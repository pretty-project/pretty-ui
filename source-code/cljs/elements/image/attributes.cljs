
(ns elements.image.attributes
    (:require [elements.image.helpers :as image.helpers]
              [pretty-css.api         :as pretty-css]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-body-attributes
  ; @ignore
  ;
  ; @param (keyword) image-id
  ; @param (map) image-props
  ; {:style (map)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :on-error (function)
  ;  :ref (function)
  ;  :style (map)}
  [image-id {:keys [style] :as image-props}]
  (-> {:class    :e-image--body
       :on-error (image.helpers/on-error-f      image-id)
       :ref      (image.helpers/set-reference-f image-id)
       :style    style}
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
  ; {}
  [_ image-props]
  (-> {:class :e-image}
      (pretty-css/default-attributes image-props)
      (pretty-css/outdent-attributes image-props)))
