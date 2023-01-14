
(ns elements.toggle.helpers
    (:require [elements.button.helpers :as button.helpers]
              [pretty-css.api          :as pretty-css]
              [hiccup.api              :as hiccup]
              [re-frame.api            :as r]
              [x.environment.api       :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)
  ;  :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-selectable (boolean)
  ;  :data-text-overflow (keyword)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [toggle-id {:keys [disabled? on-click on-mouse-over] :as toggle-props}]
  ; XXX#4460 (source-code/cljs/elements/button/helpers.cljs)
  (-> (if disabled? {:disabled           true
                     :data-selectable    false
                     :data-text-overflow :no-wrap}
                    {:data-selectable    false
                     :data-text-overflow :no-wrap
                     :id                 (hiccup/value toggle-id "body")
                     :on-click           #(r/dispatch on-click)
                     :on-mouse-over      #(r/dispatch on-mouse-over)
                     :on-mouse-up        #(x.environment/blur-element! toggle-id)
                     :data-click-effect  :opacity})
      (pretty-css/border-attributes toggle-props)
      (pretty-css/color-attributes  toggle-props)
      (pretty-css/indent-attributes toggle-props)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn toggle-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) toggle-id
  ; @param (map) toggle-props
  ;
  ; @return (map)
  [_ toggle-props]
  (-> {} (pretty-css/default-attributes toggle-props)
         (pretty-css/outdent-attributes toggle-props)))
