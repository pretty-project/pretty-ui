
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.scroll-prohibitor.effects
    (:require [re-frame.api                           :as r :refer [r]]
              [x.db.api                               :as x.db]
              [x.environment.scroll-prohibitor.events :as scroll-prohibitor.events]
              [x.environment.scroll-prohibitor.subs   :as scroll-prohibitor.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :x.environment/remove-scroll-prohibition!
  ; @param (keyword) prohibition-id
  ;
  ; @usage
  ;  [:x.environment/remove-scroll-prohibition! :my-prohibition]
  (fn [{:keys [db]} [_ prohibition-id]]
      (let [db (r x.db/remove-item! db [:x.environment :sroll-prohibitor/data-items prohibition-id])]
           (if (r scroll-prohibitor.subs/scroll-prohibiton-added? db)
               ; Ha a tiltás eltávolítása után van hozzáadva másik tiltás ...
               {:db db}
               ; Ha a tiltás eltávolítása után nincs hozzáadva másik tiltás ...
               {:db db :fx [:x.environment/enable-dom-scroll!]}))))

(r/reg-event-fx :x.environment/add-scroll-prohibition!
  ; @param (keyword) prohibition-id
  ;
  ; @usage
  ;  [:x.environment/add-scroll-prohibition! :my-prohibition]
  (fn [{:keys [db]} [_ prohibition-id]]
      (if (r scroll-prohibitor.subs/scroll-prohibiton-added? db)
          ; Ha a tiltás hozzáadása előtt volt hozzáadva másik tiltás ...
          {:db (r x.db/set-item! db [:x.environment :sroll-prohibitor/data-items prohibition-id] {})}
          ; Ha a tiltás hozzáadása előtt NEM volt hozzáadva másik tiltás ...
          {:db (r x.db/set-item! db [:x.environment :sroll-prohibitor/data-items prohibition-id] {})
           :fx [:x.environment/disable-dom-scroll!]})))

(r/reg-event-fx :x.environment/enable-scroll!
  ; @usage
  ;  [:x.environment/enable-scroll!]
  (fn [{:keys [db]} _]
      {:db (r scroll-prohibitor.events/remove-scroll-prohibitions! db)
       :fx [:x.environment/enable-dom-scroll!]}))
