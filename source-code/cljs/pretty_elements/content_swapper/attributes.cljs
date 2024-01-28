
(ns pretty-elements.content-swapper.attributes
    (:require [pretty-css.appearance.api             :as pretty-css.appearance]
              [pretty-css.basic.api                  :as pretty-css.basic]
              [pretty-css.layout.api                 :as pretty-css.layout]
              [pretty-elements.content-swapper.state :as content-swapper.state]))

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
      (pretty-css.basic/style-attributes  swapper-props)))

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
      (pretty-css.basic/class-attributes   swapper-props)
      (pretty-css.layout/outdent-attributes swapper-props)
      (pretty-css.basic/state-attributes   swapper-props)
      (pretty-css.appearance/theme-attributes   swapper-props)))
