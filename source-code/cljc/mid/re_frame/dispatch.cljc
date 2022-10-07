
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid.re-frame.dispatch
    (:require [mid.re-frame.core          :as core]
              [mid.re-frame.event-handler :as event-handler]
              [mid.re-frame.event-vector  :as event-vector]
              [mid.re-frame.metamorphic   :as metamorphic]
              [mid.re-frame.registrar     :as registrar]
              [time.api                   :as time]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(core/reg-event-fx
  ; @param (metamorphic-event) n
  ;
  ; @usage
  ;  [:dispatch-metamorphic-event [...]]
  ;
  ; @usage
  ;  [:dispatch-metamorphic-event {:dispatch [...]}]
  :dispatch-metamorphic-event
  (fn [_ [_ n]] (metamorphic/metamorphic-event->effects-map n)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn dispatch
  ; @param (metamorphic-event) event-handler
  ;
  ; @usage
  ;  (re-frame/dispatch [:foo])
  ;
  ; @usage
  ;  (re-frame/dispatch {:dispatch [:foo]})
  ;
  ; @usage
  ;  (re-frame/dispatch nil)
  [event-handler]

  ; Szerver-oldalon a Re-Frame nem jelez hibát, nem regisztrált esemény meghívásakor.
  ; A szerver-oldalon nem történnek meg a nem regisztrált Re-Frame események, ezért nem lehetséges
  ; interceptor-ban vizsgálni az események regisztráltságát.
  #?(:clj (let [event-id      (event-vector/event-vector->event-id      event-handler)
                event-exists? (event-handler/event-handler-registrated? :event event-id)]
               (if-not event-exists? (println (str "re-frame: no :event handler registrated for: " event-id)))))

  (if (vector? event-handler) (core/dispatch event-handler)
                              (core/dispatch [:dispatch-metamorphic-event event-handler])))

; @usage
;  {:dispatch ...}
(registrar/clear-handlers :fx      :dispatch)
(core/reg-fx              :dispatch dispatch)

(defn dispatch-fx
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ;  (re-frame/dispatch-fx [:my-side-effect-event ...])
  [event-handler]
  (dispatch {:fx event-handler}))

(defn dispatch-sync
  ; @param (event-vector) event-handler
  ;
  ; @usage
  ;  (re-frame/dispatch-sync [...])
  ;
  ; A dispatch-sync függvény a meghívási sebesség fontossága miatt nem kezeli
  ; a metamorphic-event kezelőket!
  [event-handler]
  (core/dispatch-sync event-handler))

(defn dispatch-n
  ; @param (metamorphic-events in vector) event-list
  ;
  ; @usage
  ;  (re-frame/dispatch-n [[:event-a]
  ;                        {:dispatch [:event-b]}
  ;                        (fn [_ _] {:dispatch [:event-c]})])
  [event-list]
  (doseq [event (remove nil? event-list)]
         (dispatch event)))

; @usage
;  {:dispatch-n [[...] [...]}
(registrar/clear-handlers :fx        :dispatch-n)
(core/reg-fx              :dispatch-n dispatch-n)

(defn dispatch-later
  ; @param (maps in vector) effects-map-list
  ;
  ; @usage
  ;  (re-frame/dispatch-later [{:ms 500 :dispatch [...]}
  ;                            {:ms 600 :fx [...]
  ;                                     :fx-n       [[...] [...]]
  ;                                     :dispatch-n [[...] [...]]}])
  [effects-map-list]
  ; Az eredeti dispatch-later függvény clojure környezetben nem időzíti a dispatch-later eseményeket!
  (doseq [{:keys [ms] :as effects-map} (remove nil? effects-map-list)]
         (if ms (letfn [(f [] (dispatch (dissoc effects-map :ms)))]
                       (time/set-timeout! f ms)))))

; @usage
;  {:dispatch-later [{...} {...}]}
(registrar/clear-handlers :fx            :dispatch-later)
(core/reg-fx              :dispatch-later dispatch-later)

(defn dispatch-if
  ; @param (*) condition
  ; @param (metamorphic-event) if-event-handler
  ; @param (metamorphic-event)(opt) else-event-handler
  ;
  ; @usage
  ;  (re-frame/dispatch-if [true [:my-event] ...])
  ;
  ; @usage
  ;  (re-frame/dispatch-if [true {:dispatch [:my-event]} ...])
  ;
  ; @usage
  ;  (re-frame/dispatch-if [true (fn [_ _] {:dispatch [:my-event]}) ...])
  [[condition if-event-handler else-event-handler]]
  (if condition (dispatch if-event-handler)
                (if else-event-handler (dispatch else-event-handler))))

; @usage
;  {:dispatch-if [...]}
(core/reg-fx :dispatch-if dispatch-if)

(defn dispatch-cond
  ; @param (vector) conditional-events
  ; [(*) condition
  ;  (metamorphic-event) if-event-handler
  ;  ...]
  ;
  ; @usage
  ;  (re-frame/dispatch-cond [(some? "a") [:my-event]
  ;                           (nil?  "b") [:my-event]])
  ;
  ; @usage
  ;  (re-frame/dispatch-cond [(some? "a") {:dispatch [:my-event]}
  ;                           (nil?  "b") {:dispatch [:my-event]}])
  ;
  ; @usage
  ;  (re-frame/dispatch-cond [(some? "a") (fn [_ _] {:dispatch [:my-event]})
  ;                           (nil?  "b") (fn [_ _] {:dispatch [:my-event]})])
  [conditional-events]
  (letfn [(dispatch-cond-f [_ dex x]
                           (if (and (even? dex) x)
                               (let [event (nth conditional-events (inc dex))]
                                    (dispatch event))))]
         (reduce-kv dispatch-cond-f nil conditional-events)))

; @usage
;  {:dispatch-cond [...]}
(core/reg-fx :dispatch-cond dispatch-cond)
