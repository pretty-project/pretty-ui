
(ns app-plugins.item-lister.sample
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :load-my-item-lister!
  (fn [_ _]
      ; Az item-lister plugin elindítható ...
      ; ... az [:my-extension.my-type-lister/load-lister! ...] esemény meghívásával.
      [:my-extension.my-type-lister/load-lister! :my-extension :my-type]
      ; ... az "/@app-home/my-extension" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension"]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.my-type-lister/add-new-item!
  ; Ha nem használsz {:new-item-options [...]} beállítást, akkor az [:my-extension.my-type-lister/add-new-item! ...]
  ; eseményt NEM szükséges létrehoznod!
  (fn [_ [_ selected-option]]
      (case selected-option :add-my-type!   [:do-something!]
                            :add-your-type! [:do-something-else!])))

(a/reg-event-fx
  :my-extension.my-type-lister/use-filter!
  ; Az [:item-lister/use-filter! ...] esemény használatával lehetséges szűrési feltételeket beállítani
  (fn [_ [_ filter-pattern]]
      [:item-lister/use-filter! :my-extension :my-type filter-pattern]))

(a/reg-event-fx
  :my-extension.my-type-lister/reload-items!
  ; Az [:item-lister/reload-items! ...] esemény újra letölti az összes elemet az aktuális
  ; beállításokkal. Így lehetséges az szerveren tárolt adatokat aktualizálni a kliens-oldalon.
  [:item-lister/reload-items! :my-extension :my-type])

(a/reg-event-fx
  :my-extension.my-type-lister/->item-clicked
  (fn [cofx [_ item-dex item]]
      [:do-something!]))



;; -- Example A ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-list-element
  [item-dex item]
  [:div "My item"])

; - A header komponens számára átadott {:menu #'...} tulajdonság beállításával lehetséges
;   egyedi menüt használni.
; - Az item-lister plugin [:item-lister/toggle-*-mode! ...] események használatával
;  tudsz a különbözű módok között váltani (több elem kiválasztása mód, rendezés mód, stb.)
(defn my-menu
  [extension-id item-namespace header-props]
  [elements/row {:content [item-lister/add-new-item-button  extension-id item-namespace header-props]
                          [item-lister/toggle-select-button extension-id item-namespace header-props]}])

(defn my-filters
  [surface-id]
  [elements/menu-bar {:menu-items [{:label "My filter"
                                    :on-click [:my-extension.my-type-lister/use-filter! {}]}]}])

(defn my-header
  [surface-id]
  [item-lister/header :my-extension :my-type {:menu #'my-menu}])

(defn my-body
  [surface-id]
  [item-lister/body :my-extension :my-type {:list-element #'my-list-element}])

; Az item-lister plugint header és body komponensre felbontva is lehetséges használni
(defn my-view
  [surface-id]
  [:<> [my-filters surface-id]
       [my-header  surface-id]
       [my-body    surface-id]])

(a/reg-event-fx
  :my-extension.my-type-lister/render-lister!
  [:ui/set-surface! :my-extension.my-type-lister/view
                    {:view #'my-view}])

(a/reg-event-fx
  :my-extension.my-type-lister/load-lister!
  [:my-extension.my-type-lister/render-lister!])



;; -- Example B ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn your-list-element
  [item-dex item]
  [:div "Your item"])

; Az item-lister plugin view komponense megjeleníti a header és a body komponenseket.
(defn your-view
  [surface-id]
  [item-lister/view :your-extension :your-type {:list-element     #'your-list-element
                                                :new-item-options [:add-my-type! :add-your-type!]}])

(a/reg-event-fx
  :your-extension.your-type-lister/render-lister!
  [:ui/set-surface! :your-extension.your-type-lister/view
                    {:view #'your-view}])

(a/reg-event-fx
  :your-extension.your-type-lister/load-lister!
  [:your-extension.your-type-lister/render-lister!])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:my-extension {:en "My extension" :hu "Kiegészítőm"}}]})
