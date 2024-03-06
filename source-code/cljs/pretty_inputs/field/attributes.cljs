
(ns pretty-inputs.field.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--input}
      (pretty-attributes/cursor-attributes props)
      ;(pretty-attributes/content-size-attributes props)
      (pretty-attributes/font-attributes         props)

      (pretty-attributes/react-attributes        props)

      (pretty-attributes/text-attributes         props)

      (pretty-attributes/input-event-attributes props)))


;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn structure-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--structure}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn inner-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--inner}
      (pretty-attributes/background-color-attributes props)
      (pretty-attributes/border-attributes           props)
      (pretty-attributes/flex-attributes             props)
      (pretty-attributes/focus-event-attributes      props)
      (pretty-attributes/inner-size-attributes       props)
      (pretty-attributes/inner-space-attributes      props)
      (pretty-attributes/input-state-attributes      props)
      (pretty-attributes/mouse-event-attributes      props)
      (pretty-attributes/style-attributes            props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn outer-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pi-field--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/state-attributes          props)
      (pretty-attributes/theme-attributes          props)))
