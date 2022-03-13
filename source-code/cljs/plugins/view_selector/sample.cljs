
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.view-selector.sample
    (:require [plugins.view-selector.api :as view-selector]
              [x.app-core.api            :as a]
              [x.app-layouts.api         :as layouts]
              [x.app-ui.api              :as ui]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/change-my-view!
  (fn [{:keys [db]} _]
      {:db (r view-selector/change-view! db :my-extension :my-view)
       :dispatch [:view-selector/change-view! :my-extension :my-view]}))



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-header
  [extension-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id extension-id])]
       [:div "My header"]))

(defn my-body
  [extension-id]
  (let [view-id @(a/subscribe [:view-selector/get-selected-view-id extension-id])]
       (case view-id :my-view   [:div "My view"]
                     :your-view [:div "Your view"]
                     [:div "Ha nem adtad meg a {:default-view-id ...} tulajdonságot ..."])))

(defn my-view
  [surface-id]
  [:<> [my-header surface-id]
       [my-body   surface-id]])

(a/reg-event-fx
  :my-extension.view-selector/render-selector!
  [:ui/set-surface! {:view #'my-view}])

; A view-selector plugin az egyes útvonalak használatakor minden alkalommal újra betöltődik,
; és betöltődéskor meghívja a [:my-extension.view-selector/load-selector! ...] eseményt.
; A felesleges renderelések elkerülése érdekében ellenőrizd le, hogy már megtörtént-e
; a renderelés!
(a/reg-event-fx
  :my-extension.view-selector/load-selector!
  (fn [{:keys [db]} _]
      (if-not (r ui/element-rendered? db :surface :my-extension.view-selector/view)
              [:my-extension.view-selector/render-selector!])))



;; -- A plugin használata "Layout A" felületen --------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  [layouts/layout-a surface-id {:header [:div "Your header"]
                                :body   [:div "Your body"]}])
