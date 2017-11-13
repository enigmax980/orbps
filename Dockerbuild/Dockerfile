FROM 10.31.57.177/dev1-ci/nginx:1.10.3

MAINTAINER ÂíµÂºÆ <madehao@e-chinalife.com>

COPY nginx.conf /usr/local/nginx-1.10.3/conf

RUN mkdir /orbps

COPY orbps /orbps

RUN chown -R nginx:nginx /orbps

# Default command
CMD ["nginx"]

