
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.docs.side-effects
    (:require [mid-fruits.candy               :refer [param return]]
              [mid-fruits.vector              :as vector]
              [server-fruits.io               :as io]
              [x.server-developer.docs.config :as docs.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn read-namespaces
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [directory-path]
  (letfn [(f [result filepath]
             (let [extension (io/filepath->extension filepath)]
                  (if (vector/contains-item? docs.config/ALLOWED-EXTENSIONS extension)
                      (let [file-content (io/read-file filepath)]
                           (assoc-in result [filepath :docs] (docs.engine/file-content->docs file-content)))
                      (return result))))]
         (let [file-list (io/all-file-list directory-path)]
              (reduce f {} file-list))))
