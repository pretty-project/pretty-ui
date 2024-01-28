
(ns pretty-engine.element.keypress.side-effects
    (:require [keypress-handler.api                     :as keypress-handler]
              [pretty-engine.element.focus.side-effects :as element.focus.side-effects]
              [pretty-engine.element.utils              :as element.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-key-pressed
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; @param (integer) key-code
  ;
  ; @usage
  ; (element-key-pressed :my-element {})
  [element-id element-props _]
  (element.focus.side-effects/focus-element! element-id element-props))

(defn element-key-released
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:on-click-f (function)(opt)}
  ; @param (integer) key-code
  ;
  ; @usage
  ; (element-key-released :my-element {:on-click-f #(...)})
  [element-id {:keys [on-click-f] :as element-props} key-code]
  (element.focus.side-effects/blur-element! element-id element-props)
  (if on-click-f (on-click-f key-code)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-element-keypress-events!
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:keypress (map)(opt)}
  ;
  ; @usage
  ; (reg-element-keypress-events! :my-element {:keypress {:key-code 13}
  ;                                            :on-click-f #(...)})
  [element-id {:keys [keypress] :as element-props}]
  (if keypress (let [on-key-id         (element.utils/element-id->subitem-id element-id :element-key-pressed)
                     on-key-pressed-f  (fn [key-code] (element-key-pressed  element-id element-props key-code))
                     on-key-released-f (fn [key-code] (element-key-released element-id element-props key-code))
                     on-key-props      (assoc keypress :on-keydown-f on-key-pressed-f :on-keyup-f on-key-released-f :prevent-default? true)]
                    (keypress-handler/reg-keypress-event! on-key-id on-key-props))))

(defn dereg-element-keypress-events!
  ; @param (keyword) element-id
  ; @param (map) element-props
  ; {:keypress (map)(opt)}
  ;
  ; @usage
  ; (dereg-element-keypress-events! :my-element {:keypress {:key-code 13}
  ;                                              :on-click-f #(...)})
  [element-id {:keys [keypress]}]
  (if keypress (let [on-key-id (element.utils/element-id->subitem-id element-id :element-key-pressed)]
                    (keypress-handler/dereg-keypress-event! on-key-id))))
