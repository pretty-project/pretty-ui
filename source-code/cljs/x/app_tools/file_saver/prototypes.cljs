
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.file-saver.prototypes
    (:require [mid-fruits.candy              :refer [param]]
              [x.app-tools.file-saver.config :as file-saver.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn saver-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;
  ; @return (map)
  ;  {:filename (string)}
  [saver-props]
  (merge {:filename file-saver.config/DEFAULT-FILENAME}
         (param saver-props)))
