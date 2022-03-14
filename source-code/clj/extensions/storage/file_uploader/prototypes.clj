
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.storage.file-uploader.prototypes
    (:require [extensions.storage.engine :as engine]
              [mongo-db.api              :as mongo-db]
              [server-fruits.io          :as io]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-item-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [file-path filename size]}]
  ; - A fájlokkal ellentétben a mappák {:media/mime-type "..."} tulajdonsága nem állapítható meg a nevükből
  ; - A fájlok {:media/mime-type "..."} tulajdonsága is eltárolásra kerül, hogy a mappákhoz hasonlóan
  ;   a fájlok is rendelkezzenek {:media/mime-type "..."} tulajdonsággal
  (let [file-id            (mongo-db/generate-id)
        generated-filename (engine/file-id->filename file-id filename)
        mime-type          (io/filename->mime-type   filename)]
       {:media/alias     filename
        :media/filename  generated-filename
        :media/filesize  size
        :media/id        file-id
        :media/mime-type mime-type
        :media/path      file-path
        :media/description ""}))
