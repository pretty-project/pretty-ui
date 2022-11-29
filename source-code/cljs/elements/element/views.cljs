
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.element.views
    (:require [x.components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-badge
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) element-id
  ; @param (map) element-props
  ;  {:badge-color (keyword)(opt)
  ;   :badge-content (metamorphic-content)(opt)}
  [_ {:keys [badge-color badge-content]}]
  ; A {:badge-content ...} tulajdonság használható, a {:badge-color ...} tulajdonság meghatározása
  ; nélkül is!
  (cond (and badge-color badge-content)
        [:div.e-element-badge {:data-color badge-color}
                              [:div.e-element-badge--content (x.components/content badge-content)]]
        badge-color   [:div.e-element-badge {:data-color badge-color}]
        badge-content [:div.e-element-badge {:data-color :primary}
                                            [:div.e-element-badge--content (x.components/content badge-content)]]))
