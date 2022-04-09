
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.mount.effects
    (:require [plugins.item-lister.core.subs     :as core.subs]
              [plugins.item-lister.mount.events  :as mount.events]
              [x.app-core.api                    :as a :refer [r]]))



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
      {:db (r mount.events/body-did-mount db lister-id body-props)
       :dispatch [:environment/listen-to-pressed-key! :item-lister/SHIFT {:key-code 16}]}))

(a/reg-event-fx
  :item-lister/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) header-props
  (fn [{:keys [db]} [_ lister-id header-props]]
      ; Az item-lister plugin header komponensében megjelenített search-field input mező
      ; tartalmát az ESC billentyű lenyomása akkor is kiüríti, ha a mező nincs fókuszált állapotban.
      ; Ehhez szükséges regisztrálni egy eseményt az ESC billentyű lenyomására, ami meghívja
      ; az [:elements/empty-field! ...] eseményt!
      ; A regisztált esemény a search-field input mező működéséhez hasonlóan az {:on-keyup ...}
      ; trigger helyett {:on-keydown ...} triggert használ.
      ; Pl.: Ha a felhasználó egy olyan listában keres, ahol elemeket lehet kijelölni, akkor
      ;      az egyes keresésekkor miután befejezte a listaelemek kijelölését, akkor az ESC billentyű
      ;      lenyomásával alaphelyzetbe állíthatja a keresést.
      ;      A keresés alaphelyzetbe állítása után az {:auto-focus? true} beállítás miatt a search-field
      ;      input mező újra fókuszált állapotba kerül, így a felhasználó beírhatja a következő kifejezést.
      {:db (r mount.events/header-did-mount db lister-id header-props)
       :dispatch [:environment/reg-keypress-event! :item-lister/ESC
                                                   {:key-code 27
                                                    :on-keydown [:elements/empty-field! :item-lister.header.views/search-items-field]}]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/body-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r mount.events/body-will-unmount db lister-id)
       :dispatch [:environment/stop-listening-to-pressed-key! :item-lister/SHIFT]}))

(a/reg-event-fx
  :item-lister/header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db (r mount.events/header-will-unmount db lister-id)
       :dispatch [:environment/remove-keypress-event! :item-lister/ESC]}))
