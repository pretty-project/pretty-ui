
; WARNING
; Az x.ui.renderer.effect névtér újraírása szükséges, a Re-Frame adatbázis
; események számának csökkentése érdekében!

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; x5 Clojure/ClojureScript web application engine
; https://monotech.hu/x5
;
; Copyright Adam Szűcs and other contributors - All rights reserved

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.renderer.effects
    (:require [hiccup.api            :as hiccup]
              [re-frame.api          :as r :refer [r]]
              [re-frame.db.api       :as r.db]
              [x.ui.renderer.config  :as renderer.config]
              [x.ui.renderer.events  :as renderer.events]
              [x.ui.renderer.helpers :as renderer.helpers]
              [x.ui.renderer.subs    :as renderer.subs]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.ui/init-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id renderer-props]]
      (let [data-items-key (renderer.helpers/data-key renderer-id :data-items)
            data-order-key (renderer.helpers/data-key renderer-id :data-order)
            meta-items-key (renderer.helpers/data-key renderer-id :meta-items)]
           {:db (as-> db % (r r.db/set-item! % [:x.ui data-items-key] {})
                           (r r.db/set-item! % [:x.ui data-order-key] [])
                           (r r.db/set-item! % [:x.ui meta-items-key] renderer-props))})))

(r/reg-event-fx :x.ui/destruct-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (map) renderer-props
  (fn [{:keys [db]} [_ renderer-id _]]))

(r/reg-event-fx :x.ui/render-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db             (r renderer.events/store-element! db renderer-id element-id element-props)
       :dispatch-later [{:ms renderer.config/REVEAL-ANIMATION-TIMEOUT :dispatch [:x.ui/rendering-ended renderer-id]}]}))

(r/reg-event-fx :x.ui/render-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r renderer.events/store-element! db renderer-id element-id element-props)
       :dispatch [:x.ui/rendering-ended renderer-id]}))

(r/reg-event-fx :x.ui/rerender-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [rerender-delay (r renderer.subs/get-rerender-delay db renderer-id element-id)]
           {:dispatch [:x.ui/destroy-element! renderer-id element-id]
            :dispatch-later
            [{:ms rerender-delay :dispatch [:x.ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(r/reg-event-fx :x.ui/update-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db (r renderer.events/update-element-props! db renderer-id element-id element-props)
       :fx [:x.environment/set-element-attribute! (hiccup/value element-id) "data-animation" "update"]
       :dispatch [:x.ui/rendering-ended renderer-id]
       :dispatch-later
       [{:ms renderer.config/UPDATE-ANIMATION-TIMEOUT
         :fx [:x.environment/remove-element-attribute! (hiccup/value element-id) "data-animation"]}]}))

(r/reg-event-fx :x.ui/update-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      {:db       (r renderer.events/update-element-props! db renderer-id element-id element-props)
       :dispatch [:x.ui/rendering-ended renderer-id]}))

(r/reg-event-fx :x.ui/push-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (let [pushing-delay            (r renderer.subs/get-pushing-delay            db renderer-id)
            lower-visible-element-id (r renderer.subs/get-lower-visible-element-id db renderer-id)]
           {:dispatch [:x.ui/destroy-element! renderer-id lower-visible-element-id]
            :dispatch-later
            [{:ms pushing-delay :dispatch [:x.ui/select-rendering-mode! renderer-id element-id element-props]}]})))

(r/reg-event-fx :x.ui/select-rendering-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (cond (r renderer.subs/push-element?            db renderer-id element-id element-props)
            [:x.ui/push-element!                         renderer-id element-id element-props]
            (r renderer.subs/rerender-element?        db renderer-id element-id element-props)
            [:x.ui/rerender-element!                     renderer-id element-id element-props]
            (r renderer.subs/update-element-animated? db renderer-id element-id element-props)
            [:x.ui/update-element-animated!              renderer-id element-id element-props]
            (r renderer.subs/update-element-static?   db renderer-id element-id element-props)
            [:x.ui/update-element-static!                renderer-id element-id element-props]
            (r renderer.subs/render-element-animated? db renderer-id element-id element-props)
            [:x.ui/render-element-animated!              renderer-id element-id element-props]
            (r renderer.subs/render-element-static?   db renderer-id element-id element-props)
            [:x.ui/render-element-static!                renderer-id element-id element-props]

            ; Ha a renderer {:queue-behavior :wait :rerender-same? false} beállítással
            ; renderelne egy már kirenderelt element, akkor egyik render esemény sem
            ; történik meg, ezért szükséges a renderert felszabadítani.
            :return [:x.ui/rendering-ended renderer-id])))

(r/reg-event-fx :x.ui/request-rendering-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  ; @param (map) element-props
  (fn [{:keys [db]} [_ renderer-id element-id element-props]]
      (if (r renderer.subs/render-element? db renderer-id element-id element-props)
          (if (r renderer.subs/render-element-now? db renderer-id element-id)
              {:db       (as-> db % (r renderer.events/reserve-renderer!  % renderer-id)
                                    (r renderer.events/update-render-log! % renderer-id element-id :render-requested-at))
               :dispatch [:x.ui/select-rendering-mode! renderer-id element-id element-props]}
              {:dispatch [:x.ui/render-element-later!  renderer-id element-id element-props]}))))

(r/reg-event-fx :x.ui/render-element-from-queue?!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (if-let [[element-id element-props] (r renderer.subs/get-next-rendering db renderer-id)]
              {:db             (r renderer.events/trim-rendering-queue! db renderer-id)
               :dispatch-later [{:ms       renderer.config/RENDER-DELAY-OFFSET
                                 :dispatch [:x.ui/request-rendering-element! renderer-id element-id element-props]}]})))

(r/reg-event-fx :x.ui/destroy-element-animated!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db (r renderer.events/mark-element-as-invisible! db renderer-id element-id)
       ; 1.
       ; Hide element ...
       :fx [:x.environment/set-element-attribute! (hiccup/value element-id) "data-animation" "hide"]
       ; 2.
       :dispatch-later
       [{:ms renderer.config/HIDE-ANIMATION-TIMEOUT
         :dispatch-n [[:x.ui/stop-element-rendering!      renderer-id element-id]
                      [:x.ui/remove-element!              renderer-id element-id]
                      [:x.ui/unmark-element-as-invisible! renderer-id element-id]
                      [:x.ui/render-element-from-queue?!  renderer-id]]}]}))

(r/reg-event-fx :x.ui/destroy-element-static!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db       (as-> db % (r renderer.events/stop-element-rendering! % renderer-id element-id)
                            (r renderer.events/remove-element!         % renderer-id element-id))
       :dispatch [:x.ui/render-element-from-queue?! renderer-id]}))

(r/reg-event-fx :x.ui/destroy-element!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  ; @param (keyword) element-id
  (fn [{:keys [db]} [_ renderer-id element-id]]
      {:db       (r renderer.events/update-render-log! db renderer-id element-id :destroyed-at)
       :dispatch (cond (r renderer.subs/destroy-element-animated? db renderer-id element-id)
                       [:x.ui/destroy-element-animated!              renderer-id element-id]
                       (r renderer.subs/destroy-element-static?   db renderer-id element-id)
                       [:x.ui/destroy-element-static!                renderer-id element-id])}))

(r/reg-event-fx :x.ui/destroy-all-elements!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      (let [destroying-event-list (r renderer.subs/get-visible-elements-destroying-event-list db renderer-id)]
           {:db             (r renderer.events/empty-rendering-queue! db renderer-id)
            :dispatch-later destroying-event-list})))

(r/reg-event-fx :x.ui/empty-renderer!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      [:x.ui/destroy-all-elements! renderer-id]))

(r/reg-event-fx :x.ui/rendering-ended
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) renderer-id
  (fn [{:keys [db]} [_ renderer-id]]
      {:db       (r renderer.events/free-renderer! db renderer-id)
       :dispatch [:x.ui/render-element-from-queue?!   renderer-id]}))
