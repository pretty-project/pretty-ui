
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-lister.sample
    (:require [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]
              [x.app-core.api              :as a]
              [x.app-elements.api          :as elements]
              [x.app-layouts.api           :as layouts]))



;; -- A plugin elindítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-lister plugin elindítható ...
(a/reg-event-fx
  :load-my-item-lister!
  (fn [_ _]
      ; ... az [:my-extension.my-type-lister/load-lister! ...] esemény meghívásával.
      [:my-extension.my-type-lister/load-lister! :my-extension :my-type]
      ; ... az "/@app-home/my-extension" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension"]))



;; -- "Új elem létrehozása" útvonal használata  -------------------------------
;; ----------------------------------------------------------------------------



;; -- "Új elem létrehozása" opciók használata  --------------------------------
;; ----------------------------------------------------------------------------

; A [:my-extension.my-type-lister/add-new-item! ...] esemény használatához szükséges beállítanod
; a {:new-item-options [...]} beállítást!
(a/reg-event-fx
  :my-extension.my-type-lister/add-new-item!
  (fn [_ [_ selected-option]]
      (case selected-option :add-my-type!   [:do-something!]
                            :add-your-type! [:do-something-else!])))



;; -- Listaelemek szűrése eseménnyel ------------------------------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/use-filter! ...] esemény használatával lehetséges szűrési feltételeket beállítani
(a/reg-event-fx
  :my-extension.my-type-lister/use-filter!
  (fn [_ [_ filter-pattern]]
      [:item-lister/use-filter! :my-extension :my-type filter-pattern]))



;; -- Szűrők használata -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  [surface-id]
  [elements/menu-bar {:menu-items [{:label "My filter" :on-click [:my-extension.my-type-lister/use-filter! {}]}]}])

(defn my-view-with-filters
  [surface-id]
  [:<> [my-filters surface-id]
       [item-lister/header :my-extension :my-type {}]
       [item-lister/body   :my-extension :my-type {}]])



;; -- Listaelemek megjelenítése előszűréssel ----------------------------------
;; ----------------------------------------------------------------------------

; A {:prefilter {...}} tulajdonság használatával beállíthatod, hogy a listában
; a szerver-oldali kollekció elemeiből csak az előszűrésnek megfelelő elemek jelenjenek meg.
(defn my-filtered-body
  [surface-id][]
  [item-lister/body :my-extension :my-type {:list-element [:div "My item"]
                                            :prefilter    {:my-type/color "red"}}])



;; -- Listaelemek frissítése a kollekció változása után -----------------------
;; ----------------------------------------------------------------------------

; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
; beállításokkal. Így lehetséges az szerveren tárolt adatokat aktualizálni a kliens-oldalon.
(a/reg-event-fx
  :my-extension.my-type-lister/reload-items!
  [:item-lister/reload-items! :my-extension :my-type])



;; -- Több listaelem kijelölése a SHIFT billentyű használatával ---------------
;; ----------------------------------------------------------------------------

(defn my-selectable-list-element
  [extension-id item-namespace item-dex item]
  [elements/toggle {:content  [:div "My item"]
                    :on-click [:my-list-element-clicked extension-id item-namespace item-dex item]}])

; Ha a listaelemek kijelölhetők és a kattintás pillanatában a SHIFT billenytű lenyomott
; állapotban volt, akkor a toggle-item-selection? függvény visszatérési értéke TRUE
(a/reg-event-fx
  :my-list-element-clicked
  (fn [{:keys [db]} [_ extension-id item-namespace item-dex item]]
      (if (r item-lister/toggle-item-selection? db extension-id item-namespace item-dex)
          {:db (r item-lister/toggle-item-selection! db extension-id item-namespace item-dex)}
          [:do-something!])))



;; -- Kontextus-menü használata a listaelemeken -------------------------------
;; ----------------------------------------------------------------------------

(defn my-contextual-list-element
  [extension-id item-namespace item-dex item]
  [elements/toggle {:content        [:div "My item"]
                    :on-click       [:do-something!]
                    :on-right-click [:render-my-context-menu!]}])

(a/reg-event-fx
  :render-my-context-menu!
  [:ui/add-popup! :my-context-menu {:body [:div "My context menu"]}])



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  [surface-id]
  [:<> [item-lister/header :my-extension :my-type {}]
       [item-lister/body   :my-extension :my-type {:list-element [:div "My item"]}]])

(a/reg-event-fx
  :my-extension.my-type-lister/render-lister!
  [:ui/set-surface! :my-extension.my-type-lister/view
                    {:view #'my-view}])

; Az [:item-lister/load-lister! ...] esemény a [:my-extension.my-type-lister/load-lister!]
; esemény meghívásával fejezi be a plugin elindítását ...
(a/reg-event-fx
  :my-extension.my-type-lister/load-lister!
  [:my-extension.my-type-lister/render-lister!])



;; -- Plugin használata "Layout A" felületeen ---------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  (let [description @(a/subscribe [:item-lister/get-description :my-extension :my-type])]
       [layouts/layout-a surface-id {:header [item-lister/header :my-extension :my-type {}]
                                     :body   [item-lister/body   :my-extension :my-type {:list-element [:div "My item"]}]
                                     :description description}]))



;; -- Egyedi menü használata --------------------------------------------------
;; ----------------------------------------------------------------------------

; - A header komponens számára átadott {:menu #'...} tulajdonság beállításával lehetséges
;   egyedi menüt használni.
; - Az item-lister plugin [:item-lister/toggle-*-mode! ...] események használatával
;  tudsz a különbözű módok között váltani (több elem kiválasztása mód, rendezés mód, stb.)
(defn my-menu
  [extension-id item-namespace header-props]
  [elements/row {:content [item-lister/add-new-item-button  extension-id item-namespace header-props]
                          [item-lister/toggle-select-button extension-id item-namespace header-props]}])

(defn my-header-with-my-menu
  [surface-id]
  [item-lister/header :my-extension :my-type {:menu #'my-menu}])



;; -- Kifejezések hozzáadaása a szótárhoz -------------------------------------
;; ----------------------------------------------------------------------------

; - Ha az item-lister plugin {:routed? true :route-title nil} beállítással van használva,
;   akkor az útvonal betöltésekor a plugin a :my-extension kifejezést beállítja az applikáció
;  címének, ezért szükséges azt hozzáadni a szótárhoz!
; - Ha az order-by-options beállításban használtál egyedi értékeket, akkor ne felejtsd el
;   hozzáadni a megfelelő szótári szavakat!
;   Pl. a :my-order/ascending értékhez tartozó kifejezés: {:by-my-order-ascending {...}}
(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:my-extension          {:en "My extension" :hu "Kiegészítőm"}
                                         :by-my-order-ascending {:en "..."          :hu "..."}}]})
