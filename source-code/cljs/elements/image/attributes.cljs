
(ns elements.image.attributes
    (:require [elements.image.utils :as image.utils]
              [pretty-css.api       :as pretty-css]
              [react.api            :as react]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:src (string)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-error (function)
  ;  :ref (function)
  ;  :src (string)
  ;  :style (map)}
  [image-id {:keys [src style] :as image-props}]
  (-> {:class    :e-image--body
       :on-error (image.utils/on-error-f image-id)
       :ref      (react/set-reference-f  image-id)
       :style    style
       :src      src}
      (pretty-css/indent-attributes image-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ image-props]
  (-> {:class :e-image}
      (pretty-css/default-attributes      image-props)
      (pretty-css/outdent-attributes      image-props)
      (pretty-css/element-size-attributes image-props)))
