
(ns pretty-elements.content-swapper.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-elements.content-swapper.state :as content-swapper.state]
              [pretty-css.layout.api :as pretty-css.layout]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn swapper-body-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-animation-direction (keyword)}
  [swapper-id swapper-props]
  (-> {:class                    :pe-content-swapper--body
       :data-animation-direction (-> @content-swapper.state/SWAPPERS swapper-id :animation-direction)}
      (pretty-css.layout/indent-attributes swapper-props)
      (pretty-css/style-attributes  swapper-props)))

(defn swapper-attributes
  ; @ignore
  ;
  ; @param (keyword) swapper-id
  ; @param (map) swapper-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ swapper-props]
  (-> {:class :pe-content-swapper}
      (pretty-css/class-attributes   swapper-props)
      (pretty-css.layout/outdent-attributes swapper-props)
      (pretty-css/state-attributes   swapper-props)
      (pretty-css/theme-attributes   swapper-props)))
