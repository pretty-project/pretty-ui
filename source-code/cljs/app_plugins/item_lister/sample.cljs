
(ns app-plugins.item-lister.sample
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-plugins.item-editor.api :as item-editor]
              [app-plugins.item-lister.api :as item-lister]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension/open-item-lister!
  ; Az item-lister plugin a "/my-extension" útvonalon érhető el
  [:router/go-to! "/my-extension"])

(a/reg-event-fx
  :my-extension/add-new-item!
  ; - A [:my-extension/add-new-item! ...] eseményt a {:new-item-options [...]} beállítással
  ;   elindított item-lister plugin működéséhez szükséges létrehozni!
  ; - Ha nem használsz {:new-item-options [...]} beállítást, akkor az [:my-extension/add-new-item! ...]
  ;   eseményt NEM szükséges létrehozni!
  (fn [_ [_ selected-option]]
      (case :add-my-type!   [:do-something!]
            :add-your-type! [:do-something-else!])))



;; -- List-item components ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-list-element
  [item-dex {:keys [id] :as item}]
  (let [editor-uri (item-editor/editor-uri :my-extension :my-type id)]
       [:button {:on-click #(a/dispatch [:router/go-to! editor-uri])}
                (str "My list item")]))



;; -- Layout components (example A) -------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-filters
  [surface-id]
  ; Az [:item-lister/use-filter! ...] esemény használatával lehetséges szűrési feltételeket beállítani
  [elements/menu-bar {:menu-items [{:label "My filter"
                                    :on-click [:item-lister/use-filter! :my-extension :my-type {}]}]}])

(defn my-view
  [surface-id]
  [:<> [my-filters surface-id]
       ; Az item-lister plugint header és body komponensre felbontva is lehetséges használni
       [item-lister/header :my-extension :my-type]
       [item-lister/body   :my-extension :my-type {:list-element #'my-list-element}]])



;; -- Layout components (example B) -------------------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  ; Az item-lister plugin view komponense megjeleníti a header és a body komponenseket.
  [item-lister/view :my-extension :my-type {:list-element #'my-list-element}])
                                          ; Új elem hozzáadásakor lehetséges több opció kiválasztását
                                          ; felajánlani:
                                          ; :new-item-options [:add-my-type! :add-your-type!]



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :my-extension/load-my-type-lister! [:ui/set-surface! {:view {:content #'my-view}}])
                                                   ; (example B)
                                                   ; [:ui/set-surface! {:view {:content #'your-view}}])

(a/reg-lifecycles
  ::lifecycles
  {:on-app-boot [:dictionary/add-terms! {:my-extension {:en "My extension" :hu "Kiegészítőm"}}]})
