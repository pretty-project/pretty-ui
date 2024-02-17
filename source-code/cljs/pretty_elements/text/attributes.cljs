
(ns pretty-elements.text.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-content-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  (-> {:class :pe-text--content}
      (pretty-attributes/content-size-attributes text-props)
      (pretty-attributes/font-attributes         text-props)
      (pretty-attributes/text-attributes         text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-body-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [text-id text-props]
  (-> {:class :pe-text--body}
      (pretty-attributes/background-color-attributes text-props)
      (pretty-attributes/border-attributes           text-props)
      (pretty-attributes/flex-attributes             text-props)
      (pretty-attributes/indent-attributes           text-props)
      (pretty-attributes/size-attributes             text-props)
      (pretty-attributes/style-attributes            text-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn text-attributes
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ text-props]
  (-> {:class :pe-text}
      (pretty-attributes/class-attributes        text-props)
      (pretty-attributes/outdent-attributes      text-props)
      (pretty-attributes/state-attributes        text-props)
      (pretty-attributes/theme-attributes        text-props)
      (pretty-attributes/wrapper-size-attributes text-props)))
