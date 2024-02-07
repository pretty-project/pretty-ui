
(ns pretty-elements.image.attributes
    (:require [pretty-attributes.api       :as pretty-attributes]
              [pretty-elements.image.utils :as image.utils]
              [react.api                   :as react]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:uri (string)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-error (function)
  ;  :ref (function)
  ;  :src (string)}
  [image-id {:keys [uri] :as image-props}]
  (-> {:class    :pe-image--body
       :on-error (image.utils/on-error-f image-id)
       :ref      (react/set-reference-f  image-id)
       :src      (-> uri)}
      (pretty-attributes/double-block-size-attributes image-props)
      (pretty-attributes/indent-attributes            image-props)
      (pretty-attributes/style-attributes             image-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ image-props]
  (-> {:class :pe-image}
      (pretty-attributes/class-attributes        image-props)
      (pretty-attributes/outdent-attributes      image-props)
      (pretty-attributes/theme-attributes        image-props)
      (pretty-attributes/state-attributes        image-props)
      (pretty-attributes/wrapper-size-attributes image-props)))
