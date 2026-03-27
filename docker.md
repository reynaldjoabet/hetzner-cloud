
Amazon Corretto 21 has a 'headless' variant available. This variant omits runtime dependencies
that are typically associated with GUI applications such as X11 and ALSA and is worth considering
for server-oriented workloads. The 'headful' variant adds support for X11 and ALSA
```sh
docker pull amazoncorretto:21.0.9-al2023-headless # 136.6MB
docker pull amazoncorretto:21.0.9-al2023-headful # 137.28MB
docker pull amazoncorretto:21.0.9-al2023 # 213.79MB
```

every file system must be mounted at a path. When you mount a file system at a directory, that directory becomes the root of the mounted file system. The original contents of the directory are hidden until the file system is unmounted. This means that if you mount a file system at a directory that already contains files, those files will not be accessible while the file system is mounted.

[](./image-17.png)

mergeddir is the root of the overlay file system. It contains the merged contents of lowerdir and upperdir. When you read from mergeddir, you see the combined contents of both lowerdir and upperdir. If there are files with the same name in both directories, the version from upperdir will be used.

when you read a file, overlayfs checks upperdir first. If the file exists in upperdir, it is returned. If the file does not exist in upperdir, overlayfs checks lowerdir. If the file exists in lowerdir, it is returned. If the file does not exist in either directory, an error is returned.

when you write to a file, overlayfs always writes to upperdir. If the file already exists in upperdir, it is overwritten. If the file does not exist in upperdir but exists in lowerdir, a copy of the file is created in upperdir and then modified. If the file does not exist in either directory, a new file is created in upperdir.

when ytou write to a file that exists in the lower layer, overlayfs creates a copy of the file in the upper layer and then modifies the copy. This is known as "copy-up". The original file in the lower layer remains unchanged. This allows you to modify files without affecting the original versions in the lower layer, which can be useful for maintaining a clean base image while allowing for changes in the upper layer.

when you run a container, all image layers become the lowerdir, and the container's writable layer becomes the upperdir. This means that any changes made to files in the container will be written to the upperdir, while the original files from the image layers remain unchanged in the lowerdir. This allows for efficient storage and isolation of changes made within the container.

overlayfs merged these two layers and the merged folder becomes the container's root file system
