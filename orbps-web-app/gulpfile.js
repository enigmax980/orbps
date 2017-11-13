// generated on 2016-08-02 using generator-webapp 2.1.0
const gulp = require('gulp');
const gulpLoadPlugins = require('gulp-load-plugins');
const browserSync = require('browser-sync');
const del = require('del');
const reporter = require('eslint-html-reporter');
const path = require('path');
const fs = require('fs');
const xml2js = require('xml2js');;
const $ = gulpLoadPlugins();
const reload = browserSync.reload;
const rename = require('gulp-rename');
const config = {
  baseDir: 'src/main/webapp/',
  projectname:'orbps/',
  tmpDir: 'target/.tmp/',
  dist: 'target/dist/',
  reports: 'report-results.html',
  tmp:'tmp/',
  production:'production'
};
var minimist = require('minimist');
var knownOptions = {
        string: 'env',
        default: { env: process.env.NODE_ENV }
};
var option =  minimist(process.argv.slice(2), knownOptions);
var target = '';

function readPOM() {
  if (target === '') {
    var parser = new xml2js.Parser({
      explicitArray: false
    });
    var data = fs.readFileSync('POM.xml', 'utf-8');
    parser.parseString(data, function(err, result) {
      target = 'target/'+result['project']['artifactId'] + '-' + result['project']['version']+'/';
      console.log(target);
    });
  }
}
gulp.task('readPOM', () => {
  readPOM();
});


gulp.task('styles', () => {
    return gulp.src([config.baseDir + '**/*.css', '!' + config.baseDir + 'src/main/webapp/**/*.min.css'])
     .pipe($.if(option.env !== config.production, $.plumber()))
    // .pipe($.plumber())
    .pipe($.stylelint({
      reporters: [
        {formatter: 'string', console: true}
      ]
    }))
    .pipe($.sourcemaps.init())
//    .pipe($.autoprefixer({
//      browsers: ['> 1%', 'last 2 versions', 'Firefox ESR']
//    }))
    .pipe($.sourcemaps.write())
  //  .pipe($.cssnano())
    .pipe(gulp.dest(target))
    .pipe(reload({
      stream: true
    }));
});

gulp.task('scripts', () => {
  return gulp.src([config.baseDir + '**/*.js', '!' + config.baseDir + '**/*.min.js'])
   .pipe($.if(option.env !== config.production,$.plumber()))
    .pipe($.sourcemaps.init())
    .pipe($.sourcemaps.write('.'))
   // .pipe($.uglify())
    .pipe(gulp.dest(config.tmp))
    .pipe(reload({
      stream: true
    }));
});

gulp.task('compressjs', () => {
	  return gulp.src([config.baseDir+'**/' +config.projectname+ '**/*.js', '!' + config.baseDir +config.projectname+'**/*.min.js'])
	   .pipe($.if(option.env !== config.production,$.plumber()))
	  //  .pipe($.rename({suffix: '.min'})) 
	    .pipe($.uglify())
	    .pipe(gulp.dest(target))
	    .pipe(reload({
	      stream: true
	    }));
	});

function lint(files, options) {
  return gulp.src(files)
    .pipe(reload({
      stream: true,
      once: true
    }))
    .pipe($.eslint(options))
    .pipe($.eslint.format())
    .pipe($.eslint.format(reporter, function(results) {
      fs.writeFileSync(path.join(__dirname, config.reports), results);
    }));
}

gulp.task('lint', () => {
  return lint([config.baseDir + '**/*.js', '!' + config.baseDir + '**/*.min.js'], {
      fix: false
    })
    .pipe(gulp.dest(config.baseDir)); //测试阶段，应该是config.baseDir
});
//设置htmlmin()的参数
var options = {
		collapseWhitespace:true,
		collapseBooleanAttributes:true,
		removeComments:true,
		removeEmptyAttributes:true,
		removeScriptTypeAttributes:true,
		removeStyleLinkTypeAttributes:true,
		minifyJS:true,
		minifyCSS:true
		};
gulp.task('html', ['readPOM','styles','compressjs', 'scripts','fonts','images'], () => {
  return gulp.src(['!'+config.baseDir + 'index.html',config.baseDir + '**/*.html'])// 测试阶段,应该是config.baseDir + '**/*.html'
    .pipe($.if('*.js', $.uglify()))
    .pipe($.if('*.css', $.cssnano({
      safe: true,
      autoprefixer: false
    })))
    .pipe($.if('*.html', $.htmlmin(options)))
    .pipe(gulp.dest(target));
});

gulp.task('images', () => {
	readPOM();
	  return gulp.src([config.baseDir + '**/*.{png,jpg,gif}'])
	    .pipe(gulp.dest(config.tmpDir))
	    .pipe(gulp.dest(target));
});

gulp.task('fonts', () => {
  readPOM();
  return gulp.src([config.baseDir + '**/*.{eot,svg,ttf,woff,woff2,min.json,json}'])
    .pipe(gulp.dest(config.tmpDir))
    .pipe(gulp.dest(target));
});

gulp.task('extras', () => {

});

gulp.task('clean', del.bind(null, ['.tmp', 'dist']));

gulp.task('serve', ['styles', 'scripts', 'fonts'], () => {
  browserSync({
    notify: false,
    port: 9000,
    server: {
      baseDir: [config.baseDir, config.tmpDir],
      routes: {

      }
    }
  });

  gulp.watch([
    config.baseDir + '**/*.html',
    config.baseDir + '**/*.{png,jpg,gif}',
    config.baseDir + '**/*.{eot,svg,ttf,woff,woff2}'
  ]).on('change', reload);

  gulp.watch(config.baseDir + '**/*.css', ['styles']);
  gulp.watch(config.baseDir + '**/*.js', ['scripts']);
  gulp.watch(config.baseDir + '**/*', ['fonts']);
});

gulp.task('serve:dist', () => {
  readPOM();
  browserSync({
    notify: false,
    port: 9000,
    server: {
      baseDir: [target]
    }
  });
});

gulp.task('serve:test', ['scripts'], () => {
  browserSync({
    notify: false,
    port: 9000,
    ui: false,
    server: {
      baseDir: 'test',
      routes: {
        '/scripts': '.tmp/scripts',
        '/bower_components': 'bower_components'
      }
    }
  });

  gulp.watch('app/scripts/**/*.js', ['scripts']);
  gulp.watch('test/spec/**/*.js').on('change', reload);
  gulp.watch('test/spec/**/*.js', ['lint:test']);
});


gulp.task('build', ['lint', 'html', 'images', 'fonts', 'extras'], () => {
  readPOM();
  return gulp.src([target + '**/*', '!' + target + '/META-INF/*', '!' + target + '/WEB-INF/*'])
   .pipe($.size({
    title: 'build',
    gzip: true
  }));
});

gulp.task('default', ['clean'], () => {
  gulp.start('build');
});
