set -e

test -f $DIR/build/.env && source $DIR/build/.env

TEST=0
PACKAGE=0
BUILD=0
BUILD_DOCKER=0
KEEP_CACHE=0
BUILD_CACHE_VOLUME=cashmanager-buildcache
DOCKER_RUN_ARGS="--rm -v $BUILD_CACHE_VOLUME:/root/ -v $DIR:/app -it"

GID=$(id -g)

for i in "$@"
do
    case $i in
        help)
            cmhelp
            exit 0
        ;;
        build)
            BUILD=1
        ;;
        test)
            TEST=1
        ;;
        package)
            PACKAGE=1
        ;;
        build-docker)
            BUILD_DOCKER=1
        ;;
        --keep-cache)
            KEEP_CACHE=1
        ;;
    esac
done

# Create the build cache
docker volume inspect $BUILD_CACHE_VOLUME &> /dev/null || docker volume create $BUILD_CACHE_VOLUME &> /dev/null
# Create the dist folder
mkdir -p $DIR/dist

test $BUILD -eq 1 && cmbuild
test $TEST -eq 1 && cmtest
test $PACKAGE -eq 1 && cmpackage
test $BUILD_DOCKER -eq 1 && cmdocker
test $KEEP_CACHE -eq 0 && docker volume rm $BUILD_CACHE_VOLUME &> /dev/null
