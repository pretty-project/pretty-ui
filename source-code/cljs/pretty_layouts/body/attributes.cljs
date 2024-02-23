
(ns pretty-layouts.body.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-body-attributes
  ; @ignore
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [body-id body-props]
  (-> {:class :pl-body--body}
      (pretty-attributes/background-color-attributes body-props)
      (pretty-attributes/border-attributes           body-props)
      (pretty-attributes/flex-attributes             body-props)
      (pretty-attributes/indent-attributes           body-props)
      (pretty-attributes/inner-size-attributes       body-props)
      (pretty-attributes/style-attributes            body-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-attributes
  ; @ignore
  ;
  ; @param (keyword) body-id
  ; @param (map) body-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ body-props]
  (-> {:class :pl-body}
      (pretty-attributes/class-attributes      body-props)
      (pretty-attributes/outdent-attributes    body-props)
      (pretty-attributes/outer-size-attributes body-props)
      (pretty-attributes/state-attributes      body-props)
      (pretty-attributes/theme-attributes      body-props)))
