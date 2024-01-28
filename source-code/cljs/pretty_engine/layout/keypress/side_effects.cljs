
(ns pretty-engine.layout.keypress.side-effects
    (:require [keypress-handler.api       :as keypress-handler]
              [pretty-engine.layout.utils :as layout.utils]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn layout-ENTER-pressed
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  ;
  ; @usage
  ; (layout-ENTER-pressed :my-layout {:on-enter-f #(...)})
  [_ {:keys [on-enter-f]}]
  (if on-enter-f (on-enter-f)))

(defn layout-ESC-pressed
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  ;
  ; @usage
  ; (layout-ESC-pressed :my-layout {:on-escape-f #(...)})
  [_ {:keys [on-escape-f]}]
  (if on-escape-f (on-escape-f)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn reg-layout-keypress-events!
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  ;
  ; @usage
  ; (reg-layout-keypress-events! :my-layout {:on-enter-f  #(...)
  ;                                          :on-escape-f #(...)})
  [layout-id {:keys [on-enter-f on-escape-f] :as layout-props}]
  ; @note (#4401)
  ; Keypress events are registered with '{:exclusive? true}' setting. Therefore, ...
  ; ... the rendered layouts don't have keypress concurrency problems.
  ; ... they are registered only if the 'on-enter-f' and/or 'on-escape-f' functions are provided.
  (let [on-enter-id         (layout.utils/layout-id->subitem-id layout-id :layout-ENTER-pressed)
        on-escape-id        (layout.utils/layout-id->subitem-id layout-id :layout-ESC-pressed)
        on-enter-pressed-f  (fn [_] (layout-ENTER-pressed layout-id layout-props))
        on-escape-pressed-f (fn [_] (layout-ESC-pressed   layout-id layout-props))
        on-enter-props      {:key-code 13 :in-type-mode? false :exclusive? true :on-keydown-f on-enter-pressed-f}
        on-escape-props     {:key-code 27 :in-type-mode? false :exclusive? true :on-keydown-f on-escape-pressed-f}]
       (if on-enter-f  (keypress-handler/reg-keypress-event! on-enter-id  on-enter-props))
       (if on-escape-f (keypress-handler/reg-keypress-event! on-escape-id on-escape-props))))

(defn dereg-layout-keypress-events!
  ; @param (keyword) layout-id
  ; @param (map) layout-props
  ; {}
  ;
  ; @usage
  ; (dereg-layout-keypress-events! :my-layout {:on-enter-f  #(...)
  ;                                            :on-escape-f #(...)})
  [layout-id {:keys [on-enter-f on-escape-f]}]
  ; @note (#4401)
  (let [on-enter-id  (layout.utils/layout-id->subitem-id layout-id :layout-ENTER-pressed)
        on-escape-id (layout.utils/layout-id->subitem-id layout-id :layout-ESC-pressed)]
       (if on-enter-f  (keypress-handler/dereg-keypress-event! on-enter-id))
       (if on-escape-f (keypress-handler/dereg-keypress-event! on-escape-id))))
