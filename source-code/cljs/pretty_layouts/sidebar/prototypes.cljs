
(ns pretty-layouts.sidebar.prototypes
    (:require [pretty-build-kit.api :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn sidebar-props-prototype
  ; @ignore
  ;
  ; @param (map) sidebar-props
  ; {:border-color (keyword or string)(opt)}
  ;
  ; @return (map)
  ; {:border-width (keyword, px or string)
  ;  :position (keyword)
  ;  :threshold (px)}
  [{:keys [border-color] :as sidebar-props}]
  (merge {:position  :left
          :threshold 720}
         (if border-color {:border-width :xxs})
         (-> sidebar-props)))
