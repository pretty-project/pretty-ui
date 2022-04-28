
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.body.effects
    (:require [plugins.item-lister.body.events :as body.events]
              [reagent.api                     :as reagent]
              [x.app-core.api                  :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) body-props
  (fn [{:keys [db]} [_ lister-id body-props]]
      ; Az item-lister plugin header komponensében megjelenített search-field input mező
      ; fókuszált állapotban a keypress-handler kezelőt {:type-mode? true} állapotba lépteti,
      ; amiért szükséges az [:environment/listen-to-pressed-key! ...] esemény használatával
      ; beállítani a SHIFT billentyű figyelését, hogy az items.subs/toggle-item-selection? függvény
      ; hozzáférjen a SHIFT billentyű állapotához (fókuszált search-field input mező esetén is).
      {:db         (r body.events/body-did-mount db lister-id body-props)
       :dispatch-n [[:environment/listen-to-pressed-key! :item-lister/SHIFT {:key-code 16}]
                    [:item-lister/request-items! lister-id]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db       (r body.events/body-will-unmount db lister-id)
       :dispatch [:environment/stop-listening-to-pressed-key! :item-lister/SHIFT]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (?) %
  (fn [{:keys [db]} [_ lister-id %]]
      (let [[_ body-props] (reagent/arguments %)]
           {:db       (r body.events/body-did-update db lister-id body-props)
            :dispatch [:tools/reload-infinite-loader! lister-id]})))
