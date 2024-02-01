
(ns pretty-elements.image.attributes
    (:require [pretty-css.appearance.api   :as pretty-css.appearance]
              [pretty-css.basic.api        :as pretty-css.basic]
              [pretty-css.layout.api       :as pretty-css.layout]
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
      (pretty-css.layout/double-block-size-attributes image-props)
      (pretty-css.layout/indent-attributes       image-props)
      (pretty-css.basic/style-attributes        image-props)))

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
      (pretty-css.basic/class-attributes        image-props)
      (pretty-css.layout/outdent-attributes      image-props)
      (pretty-css.basic/state-attributes        image-props)
      (pretty-css.appearance/theme-attributes        image-props)
      (pretty-css.layout/wrapper-size-attributes image-props)))
