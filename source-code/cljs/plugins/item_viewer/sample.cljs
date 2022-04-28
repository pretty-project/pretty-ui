
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns plugins.item-viewer.sample
    (:require [plugins.item-viewer.api :as item-viewer]
              [x.app-core.api          :as a]
              [x.app-layouts.api       :as layouts]))



;; -- Szerver-oldali beállítás ------------------------------------------------
;; ----------------------------------------------------------------------------

; A plugin beállításához mindenképpen szükséges a szerver-oldali
; [:item-viewer/init-viewer! ...] eseményt használni!



;; -- A plugin használata alapbeállításokkal ----------------------------------
;; ----------------------------------------------------------------------------

(defn my-view
  []
  [:<> [item-viewer/body   :my-viewer {:item-element [:div "My item"]}]
       [item-viewer/footer :my-viewer {}]])



;; -- A plugin használata "Layout A" felületen --------------------------------
;; ----------------------------------------------------------------------------

(defn your-view
  [surface-id]
  [layouts/layout-a ::sample
                    {:body   [item-viewer/body   :your-viewer {:item-element [:div "Your item"]}]
                     :footer [item-viewer/footer :your-viewer {}]}])



;; -- Az {:auto-title? true} beállítás használata -----------------------------
;; ----------------------------------------------------------------------------

; ...



;; -- Az [:item-viewer/view-item! "..."] esemény hanszálata -------------------
;; ----------------------------------------------------------------------------

; ...
(a/reg-event-fx
  :view-my-item!
  [:item-viewer/view-item! :my-viewer "my-item"])
