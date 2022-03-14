
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.events
    (:require [mid-fruits.candy                   :refer [param return]]
              [mid-fruits.vector                  :as vector]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-db.api                       :as db]
              [x.app-ui.api                       :as ui]
              [x.app-sync.request-handler.subs    :as request-handler.subs]
              [x.app-sync.response-handler.events :as response-handler.events]
              [x.app-sync.response-handler.subs   :as response-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-request-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  [db [_ request-id request-props]]
  (as-> db % ; DEBUG
             ; A request-id azonosítójú lekérés adatainak utolsó 256 alkalommal elküldött példányát eltárolja
             (r db/apply-item! % [:sync :request-handler/data-history request-id] vector/conj-item request-props)
             (r db/apply-item! % [:sync :request-handler/data-history request-id] vector/last-items 256)
             (r db/set-item!   % [:sync :request-handler/data-items   request-id] request-props)))

(defn request-aborted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (assoc-in db [:sync :request-handler/data-items request-id :aborted?] true))

(defn request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r a/set-process-status!   % request-id :success)
             (r a/set-process-activity! % request-id :idle)
             (if (r response-handler.subs/store-request-response?   % request-id)
                 (r response-handler.events/store-request-response! % request-id server-response)
                 (return %))))

(defn request-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ request-id server-response]]
  (as-> db % (r a/set-process-status!   % request-id :failure)
             (r a/set-process-activity! % request-id :idle)
             ; DEBUG
             ; Hiba vagy nem megfelelő szerver-válasz esetén is eltárolásra kerül a válasz ...
             (if (r response-handler.subs/store-request-response?   % request-id)
                 (r response-handler.events/store-request-response! % request-id server-response)
                 (return %))))

(defn request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:display-progress? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress?] :as request-props}]]
  (if (r request-handler.subs/request-resent? db request-id request-props)
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request újra el lett küldve ...
      (if-let [display-progress? (get-in db [:sync :request-handler/data-items request-id :display-progress?])]
              (return db)
              (r ui/stop-listening-to-process! db request-id))
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request NEM lett újra elküldve ...
      (if display-progress? (as-> db % (r a/set-process-activity!       % request-id :stalled)
                                       (r ui/stop-listening-to-process! % request-id))
                            (r a/set-process-activity! db request-id :stalled))))

(defn reset-request-process!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (as-> db % ; Set request status
             (r a/set-process-status!   % request-id :progress)
             ; Set request activity
             (r a/set-process-activity! % request-id :active)
             ; Set request progress
             ; Szükséges a process-progress értékét nullázni!
             ; A szerver-válasz megérkezése után a process-progress értéke 100%-on marad.
             (r a/set-process-progress! % request-id 0)))

(defn send-request!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:display-progress? (boolean)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress?] :as request-props}]]
  (if display-progress? (as-> db % (r reset-request-process! % request-id)
                                   (r store-request-props!   % request-id request-props)
                                   (r ui/listen-to-process!  % request-id))
                        (as-> db % (r reset-request-process! % request-id)
                                   (r store-request-props!   % request-id request-props))))
