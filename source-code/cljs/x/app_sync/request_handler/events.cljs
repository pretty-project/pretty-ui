
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-sync.request-handler.events
    (:require [mid-fruits.candy                   :refer [return]]
              [mid-fruits.time                    :as time]
              [mid-fruits.vector                  :as vector]
              [x.app-core.api                     :as a :refer [r]]
              [x.app-db.api                       :as db]
              [x.app-ui.api                       :as ui]
              [x.app-sync.request-handler.subs    :as request-handler.subs]
              [x.app-sync.response-handler.events :as response-handler.events]
              [x.app-sync.response-handler.subs   :as response-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-request-history!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  [db [_ request-id request-props]]
  ; DEBUG
  ; A request-id azonosítójú lekérés adatainak utolsó 256 alkalommal elküldött példányát eltárolja.
  (as-> db % (r db/apply-item! % [:sync :request-handler/data-history request-id] vector/conj-item request-props)
             (r db/apply-item! % [:sync :request-handler/data-history request-id] vector/last-items 256)))

(defn store-request-props!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  [db [_ request-id request-props]]
  ; Egyes függvények (pl. request-stalled) számára szükséges összehasonlítani, a paraméterként
  ; kapott request-props térképet a lekérés utolsó elküldésekor eltárolt request-props térképpel!
  (assoc-in db [:sync :request-handler/data-items request-id] request-props))

(defn store-request-sent-time!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:sent-time (string)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [sent-time] :as request-props}]]
  ; A request elküldésekor a {:sent-time "..."} tulajdonság értékét meta-adatként eltárolja,
  ; így később a request-props térképben tárolt {:sent-time "..."} tulajdonság értékével összehasonlítva
  ; megállapítható, hogy a request újra el lett-e küldve, tehát a vizsgált request-props térkép
  ; a request-id azonosítójó request legutolsó elküldéséből származik-e.
  (assoc-in db [:sync :request-handler/meta-items request-id :sent-time] sent-time))

(defn request-aborted
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (map)
  [db [_ request-id]]
  (assoc-in db [:sync :request-handler/meta-items request-id :aborted?] true))

(defn request-successed
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:response-action (keyword)}
  ; @param (*) server-response
  ;
  ; @return (map)
  [db [_ request-id {:keys [response-action] :as request-props} server-response]]
  (as-> db % (r a/set-process-status!   % request-id :success)
             (r a/set-process-activity! % request-id :idle)
             (case response-action :store (r response-handler.events/store-request-response! % request-id request-props server-response)
                                          (return %))))

(defn request-failured
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:response-action (keyword)}
  ; @param (map) server-response
  ;
  ; @return (map)
  [db [_ request-id {:keys [response-action] :as request-props} server-response]]
  ; DEBUG
  ; A szerver-válasz hiba vagy nem megfelelő esetén is eltárolásra kerül,
  ; hogy a fejlesztői eszközök hozzáférjenek a szerver-válasz értékéhez ...
  (as-> db % (r a/set-process-status!   % request-id :failure)
             (r a/set-process-activity! % request-id :idle)
             (case response-action :store (r response-handler.events/store-request-response! % request-id request-props server-response)
                                          (return %))))

(defn request-stalled
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;  {:display-progress? (boolean)(opt)
  ;   :lock-screen? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress? lock-screen?] :as request-props}]]
  ;
  ;
  (if (r request-handler.subs/request-resent? db request-id request-props)
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request újra el lett küldve ...
      (let [display-progress? (get-in db [:sync :request-handler/data-items request-id :display-progress?])
            lock-screen?      (get-in db [:sync :request-handler/data-items request-id :lock-screen?])]
           (cond-> (r a/set-process-activity! db request-id :stalled)
                   (not display-progress?) (as-> % (r ui/stop-listening-to-process! db request-id))
                   (not lock-screen?)      (as-> % (r ui/unlock-screen!             db request-id))))
      ; Ha az {:on-stalled [...]} esemény megtörténése előtt a request NEM lett újra elküldve ...
      (cond-> (r a/set-process-activity! db request-id :stalled)
              display-progress? (as-> % (r ui/stop-listening-to-process! % request-id))
              lock-screen?      (as-> % (r ui/unlock-screen!             % request-id)))))

(defn reset-request-process!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ; @param (map) request-props
  ;
  ; @return (map)
  [db [_ request-id _]]
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
  ;  {:display-progress? (boolean)(opt)
  ;   :lock-screen? (boolean)(opt)}
  ;
  ; @return (map)
  [db [_ request-id {:keys [display-progress? lock-screen?] :as request-props}]]
  (cond-> db :store-request-history!   (as-> % (r store-request-history!   % request-id request-props))
             :store-request-props!     (as-> % (r store-request-props!     % request-id request-props))
             :store-request-sent-time! (as-> % (r store-request-sent-time! % request-id request-props))
             :reset-request-process!   (as-> % (r reset-request-process!   % request-id request-props))
             display-progress?         (as-> % (r ui/listen-to-process!    % request-id))
             lock-screen?              (as-> % (r ui/lock-screen!          % request-id))))
