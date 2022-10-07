
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.window-handler.side-effects
    (:require [app-fruits.window                      :as window]
              [time.api                               :as time]
              [x.app-core.api                         :as a]
              [x.app-environment.window-handler.state :as window-handler.state]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn open-new-browser-tab!
  ; @param (string) uri
  ;
  ; @usage
  ;  (environment/open-new-browser-tab! "www.my-site.com/my-link")
  [uri]
  (.open js/window uri "_blank"))

(defn set-window-title!
  ; @param (string) title
  ;
  ; @usage
  ;  (environment/set-window-title! "My title")
  [title]
  (set! (-> js/document .-title) title))

(defn reload-window!
  ; @usage
  ;  (environment/reload-window!)
  [_]
  (.reload js/window.location true))

(defn go-to-root!
  ; @usage
  ;  (environment/go-to-root!)
  [_]
  (set! (-> js/window .-location .-href) "/"))

(defn go-to!
  ; @param (string) uri
  ;
  ; @usage
  ;  (environment/go-to! "www.my-site.com/my-link")
  [uri]
  (set! (-> js/window .-location .-href) uri))

(defn set-interval!
  ; @param (keyword) interval-id
  ; @param (map) interval-props
  ;  {:event (metamorphic-event)
  ;   :interval (ms)}
  ;
  ; @usage
  ;  (environment/set-interval! :my-interval {:event [:my-event]
  ;                                           :interval 420})
  [interval-id {:keys [interval event] :as interval-props}]
  (fn [[interval-id {:keys [interval event] :as interval-props}]]
      (letfn [(f [] (a/dispatch event))]
             (let [js-id          (time/set-interval! f interval)
                   interval-props (assoc interval-props :js-id js-id)]
                  (swap! window-handler.state/INTERVALS assoc interval-id interval-props)))))

(defn clear-interval!
  ; @param (keyword) interval-id
  ;
  ; @usage
  ;  (environment/clear-interval! :my-interval)
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
  ;  (environment/set-timeout! :my-timeout {:event [:my-event]
  ;                                         :timeout 420})
  [timeout-id {:keys [timeout event] :as timeout-props}]
  (letfn [(f [] (a/dispatch event))]
         (let [js-id         (time/set-timeout! f timeout)
               timeout-props (assoc timeout-props :js-id js-id)]
              (swap! window-handler.state/TIMEOUTS assoc timeout-id timeout-props))))

(defn clear-timeout!
  ; @param (keyword) timeout-id
  ;
  ; @usage
  ;  (environment/clear-timeout! :my-timeout)
  [timeout-id]
  (let [js-id (get-in @window-handler.state/TIMEOUTS timeout-id :js-id)]))
       ; TODO ...



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn update-window-data!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (a/dispatch [:db/set-item! [:environment :window-handler/meta-items]
                             {:language   (window/get-language)
                              :user-agent (window/get-user-agent)}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:environment/open-new-browser-tab! "www.my-site.com/my-link"]
(a/reg-fx :environment/open-new-browser-tab! open-new-browser-tab!)

; @usage
;  [:environment/set-window-title! "My title"]
(a/reg-fx :environment/set-window-title! set-window-title!)

; @usage
;  [:environment/reload-window!]
(a/reg-fx :environment/reload-window! reload-window!)

; @usage
;  [:environment/go-to-root!]
(a/reg-fx :environment/go-to-root!)

; @usage
;  [:environment/go-to! "www.my-site.com/my-link"]
(a/reg-fx :environment/go-to! go-to!)

; @usage
;  [:environment/set-interval! :my-interval {:event [:my-event] :interval 420}]
(a/reg-fx :environment/set-interval! set-interval!)

; @usage
;  [:environment/clear-interval! :my-interval]
(a/reg-fx :environment/clear-interval! clear-interval!)

; @usage
;  [:environment/set-timeout! :my-timeout {:event [:my-event] :timeout 420}]
(a/reg-fx :environment/set-timeout! set-timeout!)

; @usage
;  [:environment/clear-timeout! :my-timeout]
(a/reg-fx :environment/clear-timeout! clear-timeout!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(a/reg-fx :environment/update-window-data! update-window-data!)
