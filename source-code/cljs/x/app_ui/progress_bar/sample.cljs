
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.progress-bar.sample
    (:require [x.app-core.api :as a :refer [r]]
              [x.app-ui.api   :as ui]))



;; -- Progress bar ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a viewport felső részén megjelenő progress-bar sávon szeretnéd egy request,
; vagy bármilyen core/process folyamat állapotát megjeleníteni,
; akkor azt a [:ui/listen-to-process! ...] esemény meghívásával állíthatod be.
(a/reg-event-fx
  :listen-to-my-request!
  (fn [{:keys [db]} _]
      {; A.
       :db       (r ui/listen-to-process! db :my-request)
       ; B.
       :dispatch [:ui/listen-to-process! :my-request]}))

; Hogy minél kevesebb erőforrást igényeljen a progress-bar sáv Re-Frame feliratkozása,
; ne felejtsd el a [:stop-listening-to-process! ...] eseményt is használni!
(a/reg-event-fx
  :stop-listening-to-my-request!
  (fn [{:keys [db]} _]
      {; A.
       :db       (r ui/stop-listening-to-process! db :my-request)
       ; B.
       :dispatch [:ui/stop-listening-to-process! :my-request]}))
