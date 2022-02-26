
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-plugins.item-browser.sample
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]
              [app-plugins.item-browser.api :as item-browser]))



;; -- A plugin elindítása -----------------------------------------------------
;; ----------------------------------------------------------------------------

; Az item-browser plugin elindítható ...
(a/reg-event-fx
  :load-my-item-browser!
  (fn [_ _]
      ; ... az [:item-browser/load-browser! ...] esemény meghívásával.
      [:item-browser/load-browser! :my-extension :my-type]
      ; ...
      [:item-browser/load-browser! :my-extension :my-type {:item-id "my-item"}]
      ; ... az "/@app-home/my-extension" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension"]
      ; ... az "/@app-home/my-extension/my-item" útvonal használatával.
      [:router/go-to! "/@app-home/my-extension/my-item"]))



;; -- Szükségtelen renderelések elkerülése ------------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  [surface-id]
  [item-browser/view :my-extension :my-type {:list-element [:div "My item"]}])

(a/reg-event-fx
  :my-extension.my-type-browser/render-browser!
  [:ui/set-surface! :my-extension.my-type-browser/view
                    {:view #'my-view}])

; - Az item-browser plugin az egyes elemek böngészésekor minden alkalommal újra betöltődik,
;   és betöltődéskor meghívja a [:my-extension.my-type-browser/load-browser! ...] eseményt.
; - A felesleges renderelések elkerülése érdekében ellenőrizd le, hogy már megtörtént-e
;   a renderelés!
(a/reg-event-fx
  :my-extension.my-type-browser/load-browser!
  (fn [{:keys [db]} _]
      (if-not (r ui/element-rendered? db :surface :my-extension.my-type-browser/view)
              [:my-extension.my-type-browser/render-browser!])))
