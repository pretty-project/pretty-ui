
(ns pretty-elements.toggle.attributes
    (:require [pretty-attributes.api :as pretty-attributes]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-inner-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ toggle-props]
  (-> {:class :pe-toggle--inner}
      (pretty-attributes/anchor-attributes           toggle-props)
      (pretty-attributes/background-color-attributes toggle-props)
      (pretty-attributes/border-attributes           toggle-props)
      (pretty-attributes/clickable-state-attributes  toggle-props)
      (pretty-attributes/cursor-attributes           toggle-props)
      (pretty-attributes/effect-attributes           toggle-props)
      (pretty-attributes/flex-attributes             toggle-props)
      (pretty-attributes/indent-attributes           toggle-props)
      (pretty-attributes/inner-size-attributes       toggle-props)
      (pretty-attributes/mouse-event-attributes      toggle-props)
      (pretty-attributes/react-attributes            toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  ...}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-attributes/class-attributes          toggle-props)
      (pretty-attributes/inner-position-attributes toggle-props)
      (pretty-attributes/outdent-attributes        toggle-props)
      (pretty-attributes/outer-position-attributes toggle-props)
      (pretty-attributes/outer-size-attributes     toggle-props)
      (pretty-attributes/state-attributes          toggle-props)
      (pretty-attributes/theme-attributes          toggle-props)))
