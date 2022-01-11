
(ns server-extensions.storage.engine
    (:require [mid-extensions.storage.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid-extensions.storage.engine
(def ROOT-DIRECTORY-ID    engine/ROOT-DIRECTORY-ID)
(def SAMPLE-FILE-ID       engine/SAMPLE-FILE-ID)
(def SAMPLE-FILE-FILENAME engine/SAMPLE-FILE-FILENAME)



;; -- Attach item functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn attach-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ; @param (string) file-id
  ;
  ; @return (?)
  [directory-id file-id])


(defn attach-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ; @param (string) file-id
  ;
  ; @return (?)
  [directory-id file-id])



;; -- Detach item functions ---------------------------------------------------
;; ----------------------------------------------------------------------------

(defn detach-file!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ; @param (string) file-id
  ;
  ; @return (?)
  [directory-id file-id]
  (let [file-link {:file/id file-id}]))


(defn detach-directory!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) directory-id
  ; @param (string) file-id
  ;
  ; @return (?)
  [directory-id file-id])
