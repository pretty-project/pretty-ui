
(ns app-plugins.item-lister.sample
    (:require [x.app-core.api :as a]
              [app-plugins.item-lister.api :as item-lister]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A szerver-oldalon inicializált item-lister plugint az extension nevéből készített
; útvonalon lehetséges elérni
(a/dispatch [:router/go-to! "/my-extension"])



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-list-item
  [item-dex {:keys [id] :as item}]
  (let [editor-uri (item-editor/editor-uri :my-extension :my-type id)]
       [:button {:on-click #(a/dispatch [:router/go-to! editor-uri])}
                "My list item"]))

(defn my-view
  [surface-id]
  ; Az item-lister plugint header és body komponensre felbontva is lehetséges használni
  [:<> [item-lister/header :my-extension :my-type]
       [item-lister/body   :my-extension :my-type {:list-item #'my-list-item}]])

(defn your-view
  [surface-id]
  ; Az item-lister plugin view komponense megjeleníti a header és a body komponenseket.
  [item-lister/view :my-extension :my-type {:list-item #'my-list-item}])



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx :my-extension/render-my-type-lister! [:ui/set-surface! {:view {:content #'my-view}}])
                                                    ;[:ui/set-surface! {:view {:content #'your-view}}])
