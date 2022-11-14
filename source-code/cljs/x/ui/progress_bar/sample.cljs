
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.progress-bar.sample
    (:require [re-frame.api :as r :refer [r]]
              [x.ui.api     :as x.ui]))



;; -- Progress bar ------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a viewport felső részén megjelenő progress-bar sávon szeretnéd egy request,
; vagy bármilyen core/process folyamat állapotát megjeleníteni,
; akkor azt a [:x.ui/listen-to-process! ...] esemény meghívásával állíthatod be.
(r/reg-event-fx :listen-to-my-request!
  (fn [{:keys [db]} _]
      {; A.
       :db       (r x.ui/listen-to-process! db :my-request)
       ; B.
       :dispatch [:x.ui/listen-to-process! :my-request]}))

; Hogy minél kevesebb erőforrást igényeljen a progress-bar sáv Re-Frame feliratkozása,
; ne felejtsd el a [:stop-listening-to-process! ...] eseményt is használni!
(r/reg-event-fx :stop-listening-to-my-request!
  (fn [{:keys [db]} _]
      {; A.
       :db       (r x.ui/stop-listening-to-process! db :my-request)
       ; B.
       :dispatch [:x.ui/stop-listening-to-process! :my-request]}))
