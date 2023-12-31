
(ns pretty-elements.toggle.attributes
    (:require [dom.api           :as dom]
              [fruits.hiccup.api :as hiccup]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (function or Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (function or Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)
  ;  :data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :data-text-overflow (keyword)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [toggle-id {:keys [disabled? on-click on-mouse-over] :as toggle-props}]
  ; @note (pretty-elements.button.attributes#4460)
  (-> (if disabled? {:class              :pe-toggle--body
                     :disabled           true
                     :data-selectable    false
                     :data-text-overflow :hidden}
                    {:class              :pe-toggle--body
                     :data-click-effect  :opacity
                     :data-selectable    false
                     :data-text-overflow :hidden
                     :id                 (hiccup/value toggle-id "body")
                     :on-click           #(pretty-build-kit/dispatch-event-handler! on-click)
                     :on-mouse-over      #(pretty-build-kit/dispatch-event-handler! on-mouse-over)
                     :on-mouse-up        #(dom/blur-active-element!)})
      (pretty-build-kit/border-attributes       toggle-props)
      (pretty-build-kit/color-attributes        toggle-props)
      (pretty-build-kit/cursor-attributes       toggle-props)
      (pretty-build-kit/element-size-attributes toggle-props)
      (pretty-build-kit/indent-attributes       toggle-props)
      (pretty-build-kit/link-attributes         toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  ; {:class (keyword or keywords in vector)}
  [_ toggle-props]
  (-> {:class :pe-toggle}
      (pretty-build-kit/class-attributes        toggle-props)
      (pretty-build-kit/outdent-attributes      toggle-props)
      (pretty-build-kit/state-attributes        toggle-props)
      (pretty-build-kit/wrapper-size-attributes toggle-props)))
