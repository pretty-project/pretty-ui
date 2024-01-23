
(ns pretty-engine.element.keypress.side-effects)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-ENTER-pressed
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [_ {:keys [on-enter-f]}]
  (if on-enter-f (on-enter-f)))

(defn element-ESC-pressed
  ; @ignore
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {}
  [_ {:keys [on-escape-f]}]
  (if on-escape-f (on-escape-f)))
