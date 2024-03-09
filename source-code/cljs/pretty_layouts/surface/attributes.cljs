
(ns pretty-layouts.surface.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-attributes
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ props]
  (-> {:class :pl-surface--content}
      (pretty-attributes/content-size-attributes props)
      (pretty-attributes/font-attributes         props)
      (pretty-attributes/overflow-attributes     props)
      (pretty-attributes/text-attributes         props)))

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
  (-> {:class :pl-surface--inner}
      (pretty-attributes/animation-attributes        props)
      (pretty-attributes/background-color-attributes props)
      (pretty-attributes/flex-attributes             props)
      (pretty-attributes/inner-size-attributes       props)
      (pretty-attributes/inner-space-attributes      props)
      (pretty-attributes/mouse-event-attributes      props)
      (pretty-attributes/state-attributes            props)
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
  (-> {:class :pl-surface--outer}
      (pretty-attributes/class-attributes          props)
      (pretty-attributes/inner-position-attributes props)
      (pretty-attributes/outer-position-attributes props)
      (pretty-attributes/outer-size-attributes     props)
      (pretty-attributes/outer-space-attributes    props)
      (pretty-attributes/theme-attributes          props)
      (pretty-attributes/visibility-attributes     props)))
