
(ns app-plugins.item-lister.sample
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]
              [app-plugins.item-lister.api :as item-lister]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :open-item-lister!
  [:router/go-to! "/my-extension"])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-list-item
  [item-dex {:keys [id] :as item}]
  (let [editor-uri (item-editor/editor-uri :my-extension :my-type id)]
       [:button {:on-click #(a/dispatch [:router/go-to! editor-uri])}
                (str "My list item")]))

(defn my-filters
  [surface-id]
  [elements/menu-bar {:menu-items [{:label "My filter"
                                    :on-click [:item-lister/use-filter! :my-extension :my-type :my-filter]}]}])

(defn my-view
  [surface-id]
  [:<> [my-filters surface-id]
       ; Az item-lister plugint header és body komponensre felbontva is lehetséges használni
       [item-lister/header :my-extension :my-type]
       [item-lister/body   :my-extension :my-type {:list-item #'my-list-item}]])

(defn your-view
  [surface-id]
  ; Az item-lister plugin view komponense megjeleníti a header és a body komponenseket.
  [item-lister/view :my-extension :my-type {:list-item #'my-list-item}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :my-extension/render-my-type-lister! [:ui/set-surface! {:view {:content #'my-view}}])
                                                    ;[:ui/set-surface! {:view {:content #'your-view}}])
