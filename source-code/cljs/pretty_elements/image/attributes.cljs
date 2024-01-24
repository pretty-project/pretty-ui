
(ns pretty-elements.image.attributes
    (:require [pretty-css.api :as pretty-css]
              [pretty-elements.image.utils :as image.utils]
              [react.api                   :as react]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:src (string)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-error (function)
  ;  :ref (function)
  ;  :src (string)}
  [image-id {:keys [src] :as image-props}]
  (-> {:class    :pe-image--body
       :on-error (image.utils/on-error-f image-id)
       :ref      (react/set-reference-f  image-id)
       :src      (-> src)}
      (pretty-css/element-size-attributes image-props)
      (pretty-css/indent-attributes       image-props)
      (pretty-css/style-attributes        image-props)))

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
  (-> {:class :pe-image}
      (pretty-css/class-attributes        image-props)
      (pretty-css/outdent-attributes      image-props)
      (pretty-css/state-attributes        image-props)
      (pretty-css/wrapper-size-attributes image-props)))
