
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.bubbles.effects
    (:require [re-frame.api            :as r :refer [r]]
              [x.ui.bubbles.config     :as bubbles.config]
              [x.ui.bubbles.prototypes :as bubbles.prototypes]
              [x.ui.bubbles.subs       :as bubbles.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/initialize-bubble!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
      {:dispatch-later [(if (r bubbles.subs/autoclose-bubble? db bubble-id)
                            {:ms bubbles.config/BUBBLE-LIFETIME :dispatch [:x.ui/autoclose-bubble?! bubble-id]})]}))

(r/reg-event-fx :x.ui/autoclose-bubble?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) bubble-id
  (fn [{:keys [db]} [_ bubble-id]]
            ; Ha a bubble rendelkezik {:autoclose? true} tulajdonsággal és lejárt az ideje,
            ; akkor megtörténik a close esemény.
      (cond (and (r bubbles.subs/autoclose-bubble?        db bubble-id)
                 (r bubbles.subs/bubble-lifetime-elapsed? db bubble-id))
            [:x.ui/remove-bubble! bubble-id]
            ; Ha a bubble rendelkezik {:autoclose? true} tulajdonsággal és még NEM járt le az ideje,
            ; akkor beidőzít egy autoclose eseményt a bubble várható élettartamára.
            ; Előfordulhat, hogy a bubble hátralévő idejében újra meghívásra kerül a render esemény,
            ; ami miatt újraindul a bubble élettartama.
            (r bubbles.subs/autoclose-bubble? db bubble-id)
            {:dispatch-later [{:ms (r bubbles.subs/get-bubble-lifetime-left db bubble-id)
                               :dispatch [:x.ui/autoclose-bubble?! bubble-id]}]})))

(r/reg-event-fx :x.ui/remove-bubble!
  ; @param (keyword) bubble-id
  ; @param (map)(opt) action-props
  ;  {:timeout (ms)(opt)
  ;    Default: 0}
  ;
  ; @usage
  ;  [:x.ui/remove-bubble! :my-bubble]
  (fn [{:keys [db]} [_ bubble-id {:keys [timeout]}]]
      (if timeout {:dispatch-later [{:ms timeout :dispatch [:x.ui/destroy-element! :bubbles bubble-id]}]}
                  [:x.ui/destroy-element! :bubbles bubble-id])))

(r/reg-event-fx :x.ui/render-bubble!
  ; @param (keyword)(opt) bubble-id
  ; @param (map) bubble-props
  ;  {:autoclose? (boolean)(opt)
  ;    Default: true
  ;   :body (metamorphic-content)
  ;   :on-mount (metamorphic-event)(opt)
  ;   :on-unmount (metamorphic-event)(opt)
  ;   :user-close? (boolean)(opt)
  ;    Default: true}
  ;
  ; @usage
  ;  [:x.ui/render-bubble! {...}]
  ;
  ; @usage
  ;  [:x.ui/render-bubble! :my-bubble {...}]
  [r/event-vector<-id]
  (fn [{:keys [db]} [_ bubble-id bubble-props]]
      (let [bubble-props (bubbles.prototypes/bubble-props-prototype bubble-id bubble-props)]
           {:dispatch-if [(r bubbles.subs/bubbles-enabled-by-user? db)
                          [:x.ui/request-rendering-element! :bubbles bubble-id bubble-props]]})))
