
(ns pretty-elements.toggle.attributes
    (:require [dom.api           :as dom]
              [fruits.hiccup.api :as hiccup]
              [pretty-css.api    :as pretty-css]
              [pretty-elements.element.side-effects :as element.side-effects]))

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
                     :on-click           #(element.side-effects/dispatch-event-handler! on-click)
                     :on-mouse-over      #(element.side-effects/dispatch-event-handler! on-mouse-over)
                     :on-mouse-up        #(dom/blur-active-element!)})
      (pretty-css/border-attributes       toggle-props)
      (pretty-css/color-attributes        toggle-props)
      (pretty-css/cursor-attributes       toggle-props)
      (pretty-css/element-size-attributes toggle-props)
      (pretty-css/indent-attributes       toggle-props)
      (pretty-css/link-attributes         toggle-props)))

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
      (pretty-css/class-attributes        toggle-props)
      (pretty-css/state-attributes        toggle-props)
      (pretty-css/outdent-attributes      toggle-props)
      (pretty-css/wrapper-size-attributes toggle-props)))
