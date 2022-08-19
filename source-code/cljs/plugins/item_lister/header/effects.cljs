

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-lister.header.effects
    (:require [plugins.item-lister.header.events :as header.events]
              [reagent.api                     :as reagent]
              [x.app-core.api                    :as a :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/header-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (map) header-props
  (fn [{:keys [db]} [_ lister-id header-props]]
      ; XXX#6779
      ; Az item-lister plugin header komponensében megjelenített search-field input mező
      ; tartalmát az ESC billentyű lenyomása akkor is kiüríti, ha a mező nincs fókuszált állapotban.
      ; Ehhez szükséges regisztrálni egy eseményt az ESC billentyű lenyomására, ami meghívja
      ; az [:elements/empty-field! ...] eseményt!
      ; A regisztált esemény a search-field input mező működéséhez hasonlóan az {:on-keyup ...}
      ; trigger helyett {:on-keydown ...} triggert használ.
      ; Pl.: Ha a felhasználó egy olyan listában keres, ahol elemeket lehet kijelölni, akkor
      ;      az egyes keresésekkor miután befejezte a listaelemek kijelölését, akkor az ESC billentyű
      ;      lenyomásával GYORSAN alaphelyzetbe állíthatja a keresést.
      ;      A keresés alaphelyzetbe állítása után az {:auto-focus? true} beállítás miatt a search-field
      ;      input mező újra fókuszált állapotba kerül, így a felhasználó beírhatja a következő kifejezést.
      {:db       (r header.events/header-did-mount db lister-id header-props)
       :dispatch (let [on-keydown [:elements/empty-field! :plugins.item-lister.header.views/search-items-field]]
                      [:environment/reg-keypress-event! :item-lister/ESC
                                                        {:key-code   27
                                                         :on-keydown on-keydown}])}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/header-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  (fn [{:keys [db]} [_ lister-id]]
      {:db       (r header.events/header-will-unmount db lister-id)
       :dispatch [:environment/remove-keypress-event! :item-lister/ESC]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :item-lister/header-did-update
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) lister-id
  ; @param (?) %
  (fn [{:keys [db]} [_ lister-id %]]
      (let [[_ header-props] (reagent/arguments %)]
           {:db (r header.events/header-did-update db lister-id header-props)})))
