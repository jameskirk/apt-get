download() {
	if [ ! -d "$WORKDIR" ]; then
	    mkdir $WORKDIR
	fi
	wget $SRC_URI -e use_proxy=yes -e http_proxy=$HTTP_PROXY -e https_proxy=$HTTPS_PROXY
	echo ${SRC_URI##*/}
}

unpack() {
	for FILE in "$WORKDIR"/*
	do
	  echo $FILE
	  if [ -f "$FILE" ]; then
	      if [[ "$FILE" == *.zip ]]; then
	        unzip $FILE
	      fi
	      if [[ "$FILE" == *.7z ]]; then
	        7z x $FILE
	      fi
	  fi
	done
}

install() {
	for FILE in "$WORKDIR"/*
	do
	  echo $FILE
	  if [ -f "$FILE" ]; then
	      if [[ "$FILE" == *.exe ]] || [[ "$FILE" == *.msi ]]; then
	        einstall $FILE
	        break
	      fi
	  fi
	done
}

clean() {
	rm -rfv ${WORKDIR}/*
}

remove() {
	eremove
}