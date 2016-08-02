/**
 * Created by Administrator on 2016/7/31.
 */
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var del = require('del');


gulp.task('html', function (){
    return gulp.src('app/*.html')
        .pipe($.useref())
        .pipe($.if('*.js', $.uglify()))
        .pipe($.if('*.css', $.cssnano({safe: true, autoprefixer: false})))
        .pipe($.if('*.html', $.htmlmin({collapseWhitespace: true})))
        .pipe(gulp.dest('dist'));
});

gulp.task('browser-sync', function() {//注册任务
    browserSync({//调用API
        files: "**",//监听整个项目
        server: {
            baseDir: "./app"
        }
    });
});

gulp.task('clean', function () {
    return del('dist');
});

gulp.task('build', ['clean', 'html']);