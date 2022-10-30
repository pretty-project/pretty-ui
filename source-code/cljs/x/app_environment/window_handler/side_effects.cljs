
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.window-handler.side-effects
    (:require [dom.api                                :as dom]
              [re-frame.api                           :as r]
              [time.api                               :as time]
              [window.api                             :as window]
              [x.app-environment.window-handler.state :as window-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-tab-title!
  ; @param (string) title
  ;
  ; @usage
  ;  (set-tab-title! "My title")
  [title]
  (dom/set-document-title! title))

(defn open-tab!
  ; @param (string) uri
  ;
  ; @usage
  ;  (open-tab! "www.my-site.com/my-link")
  [uri]
  (window/open-tab!))

(defn reload-tab!
  ; @usage
  ;  (reload-tab!)
  [_]
  (window/reload-tab!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-root!
  ; @usage
  ;  (go-root!)
  [_]
  (window/go-root!))

(defn go-back!
  ; @usage
  ;  (go-back!)
  [_]
  (window/go-back!))

(defn go-to!
  ; @param (string) uri
  ;
  ; @usage
  ;  (go-to! "www.my-site.com/my-link")
  [uri]
  (window/go-to! uri))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-interval!
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  ;
  ; @usage
  ;  (set-interval! :my-interval {:event [:my-event]
  ;                               :interval 420})
  [interval-id {:keys [interval event] :as interval-props}]
  (fn [[interval-id {:keys [interval event] :as interval-props}]]
      (letfn [(f [] (r/dispatch event))]
             (let [js-id          (time/set-interval! f interval)
                   interval-props (assoc interval-props :js-id js-id)]
                  (swap! window-handler.state/INTERVALS assoc interval-id interval-props)))))

(defn clear-interval!
  ; @param (keyword) interval-id
  ;
  ; @usage
  ;  (clear-interval! :my-interval)
  [interval-id]
  (let [js-id (get-in @window-handler.state/INTERVALS interval-id :js-id)]
       (time/clear-interval! js-id)))

(defn set-timeout!
  ; @param (keyword) timeout-id
  ; @param (map) timeout-props
  ;  {:event (metamorphic-event)
  ;   :timeout (ms)}
  ;
  ; @usage
  ;  (set-timeout! :my-timeout {:event [:my-event]
  ;                             :timeout 420})
  [timeout-id {:keys [timeout event] :as timeout-props}]
  (letfn [(f [] (r/dispatch event))]
         (let [js-id         (time/set-timeout! f timeout)
               timeout-props (assoc timeout-props :js-id js-id)]
              (swap! window-handler.state/TIMEOUTS assoc timeout-id timeout-props))))

(defn clear-timeout!
  ; @param (keyword) timeout-id
  ;
  ; @usage
  ;  (clear-timeout! :my-timeout)
  [timeout-id]
  (let [js-id (get-in @window-handler.state/TIMEOUTS timeout-id :js-id)]))
       ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-window-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (r/dispatch [:db/set-item! [:environment :window-handler/meta-items]
                             {:language   (window/get-language)
                              :user-agent (window/get-user-agent)}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/set-tab-title! "My title"]
(r/reg-fx :environment/set-tab-title! set-tab-title!)

; @usage
;  [:environment/open-tab! "www.my-site.com/my-link"]
(r/reg-fx :environment/open-tab! open-tab!)

; @usage
;  [:environment/reload-tab!]
(r/reg-fx :environment/reload-tab! reload-tab!)

; @usage
;  [:environment/go-root!]
(r/reg-fx :environment/go-root! go-root!)

; @usage
;  [:environment/go-back!]
(r/reg-fx :environment/go-back! go-back!)

; @usage
;  [:environment/go-to! "www.my-site.com/my-link"]
(r/reg-fx :environment/go-to! go-to!)

; @usage
;  [:environment/set-interval! :my-interval {:event [:my-event] :interval 420}]
(r/reg-fx :environment/set-interval! set-interval!)

; @usage
;  [:environment/clear-interval! :my-interval]
(r/reg-fx :environment/clear-interval! clear-interval!)

; @usage
;  [:environment/set-timeout! :my-timeout {:event [:my-event] :timeout 420}]
(r/reg-fx :environment/set-timeout! set-timeout!)

; @usage
;  [:environment/clear-timeout! :my-timeout]
(r/reg-fx :environment/clear-timeout! clear-timeout!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :environment/update-window-data! update-window-data!)
