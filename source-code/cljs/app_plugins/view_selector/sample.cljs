
(ns app-plugins.view-selector.sample
    (:require [x.app-core.api :as a]
              [x.app-ui.api   :as ui]
              [app-plugins.view-selector.api :as view-selector]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/how-to-start?
  (fn [_ _]
      ; A view-selector plugin elindítható ...
      ; ... a [:view-selector/load-selector! ...] esemény meghívásával.
      [:view-selector/load-selector! :my-extension]
      ; ... a [:view-selector/go-to! ...] esemény meghívásával.
      [:view-selector/go-to! :my-extension :my-view]
      ; ... az "/@app-home/my-extension" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension"]
      ; ... az "/@app-home/my-extension/my-view" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension/my-view"]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :my-extension.view-selector/change-my-view!
  (fn [{:keys [db]} _]
      {:db (r view-selector/change-view! db :my-extension :my-view)
       :dispatch [:view-selector/change-view! :my-extension :my-view]}))



;; -- Example A ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; A view-selector plugint header és body komponensre felbontva is lehetséges használni



;; -- Example B ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn my-header
  [extension-id {:keys [view-id]}]
  [:div "My header"])

(defn my-body
  [extension-id {:keys [view-id]}]
  (case view-id :my-view   [:div "My view"]
                :your-view [:div "Your view"]
                [:div "Ha nem adtad meg a {:default-view-id ...} tulajdonságot ..."]))

(defn my-view
  [surface-id]
  [view-selector/view {:body   #'my-body
                       :header #'my-header}])

(a/reg-event-fx
  :my-extension.view-selector/render-selector!
  [:ui/set-surface! {:view #'my-view}])

(a/reg-event-fx
  :my-extension.view-selector/load-selector!
  (fn [{:keys [db]} _]
      ; A view-selector plugin az egyes útvonalak használatakor minden alkalommal újra betöltődik,
      ; és betöltődéskor meghívja a [:my-extension.view-selector/load-selector! ...] eseményt.
      ; A felesleges renderelések elkerülése érdekében ellenőrizd le, hogy már megtörtént-e
      ; a renderelés!
      (if-not (r ui/element-rendered? db :surface :my-extension.view-selector/view)
              [:my-extension.view-selector/render-selector!])))
