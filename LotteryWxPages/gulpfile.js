/**
 * Created by Administrator on 2016/7/31.
 */
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();
var del = require('del');
var runSequence = require('run-sequence');

gulp.task('html', function (){
    return gulp.src('app/*.html')
        .pipe($.useref())
        .pipe($.if('*.js', $.uglify()))
        .pipe($.if('*.css', $.cssnano({safe: true, autoprefixer: false})))
        .pipe($.if('*.html', $.htmlmin({collapseWhitespace: true})))
        .pipe(gulp.dest('dist'));
});

gulp.task('clean', function () {
    return del('dist');
});

gulp.task('build', function () {
    runSequence('clean',
        ['html']);
});