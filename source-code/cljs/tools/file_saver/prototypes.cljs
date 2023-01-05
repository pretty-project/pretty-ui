
(ns tools.file-saver.prototypes
    (:require [candy.api               :refer [param]]
              [tools.file-saver.config :as config]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn saver-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) saver-props
  ;
  ; @return (map)
  ; {:filename (string)}
  [saver-props]
  (merge {:filename config/DEFAULT-FILENAME}
         (param saver-props)))
