
(ns elements.toggle.attributes
    (:require [dom.api        :as dom]
              [pretty-css.api :as pretty-css]
              [hiccup.api     :as hiccup]
              [re-frame.api   :as r]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; @ignore
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (Re-Frame metamorphic-event)(opt)
  ;  :on-mouse-over (Re-Frame metamorphic-event)(opt)}
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
  ; XXX#4460 (source-code/cljs/elements/button/utils.cljs)
  (-> (if disabled? {:class              :e-toggle--body
                     :disabled           true
                     :data-selectable    false
                     :data-text-overflow :no-wrap}
                    {:class              :e-toggle--body
                     :data-click-effect  :opacity
                     :data-selectable    false
                     :data-text-overflow :no-wrap
                     :id                 (hiccup/value toggle-id "body")
                     :on-click           #(r/dispatch on-click)
                     :on-mouse-over      #(r/dispatch on-mouse-over)
                     :on-mouse-up        #(dom/blur-active-element!)})
      (pretty-css/border-attributes toggle-props)
      (pretty-css/color-attributes  toggle-props)
      (pretty-css/cursor-attributes toggle-props)
      (pretty-css/indent-attributes toggle-props)
      (pretty-css/link-attributes   toggle-props)))

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
  (-> {:class :e-toggle}
      (pretty-css/default-attributes      toggle-props)
      (pretty-css/outdent-attributes      toggle-props)
      (pretty-css/element-size-attributes toggle-props)))
