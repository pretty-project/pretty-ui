
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.window-handler.side-effects
    (:require [dom.api                            :as dom]
              [js-window.api                      :as js-window]
              [re-frame.api                       :as r]
              [time.api                           :as time]
              [x.environment.window-handler.state :as window-handler.state]))



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
  (js-window/open-tab! uri))

(defn reload-tab!
  ; @usage
  ;  (reload-tab!)
  [_]
  (js-window/reload-tab!))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn go-root!
  ; @usage
  ;  (go-root!)
  [_]
  (js-window/go-root!))

(defn go-back!
  ; @usage
  ;  (go-back!)
  [_]
  (js-window/go-back!))

(defn go-to!
  ; @param (string) uri
  ;
  ; @usage
  ;  (go-to! "www.my-site.com/my-link")
  [uri]
  (js-window/go-to! uri))



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
  (r/dispatch [:x.db/set-item! [:x.environment :window-handler/meta-items]
                               {:language   (js-window/get-language)
                                :user-agent (js-window/get-user-agent)}]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.environment/set-tab-title! "My title"]
(r/reg-fx :x.environment/set-tab-title! set-tab-title!)

; @usage
;  [:x.environment/open-tab! "www.my-site.com/my-link"]
(r/reg-fx :x.environment/open-tab! open-tab!)

; @usage
;  [:x.environment/reload-tab!]
(r/reg-fx :x.environment/reload-tab! reload-tab!)

; @usage
;  [:x.environment/go-root!]
(r/reg-fx :x.environment/go-root! go-root!)

; @usage
;  [:x.environment/go-back!]
(r/reg-fx :x.environment/go-back! go-back!)

; @usage
;  [:x.environment/go-to! "www.my-site.com/my-link"]
(r/reg-fx :x.environment/go-to! go-to!)

; @usage
;  [:x.environment/set-interval! :my-interval {:event [:my-event] :interval 420}]
(r/reg-fx :x.environment/set-interval! set-interval!)

; @usage
;  [:x.environment/clear-interval! :my-interval]
(r/reg-fx :x.environment/clear-interval! clear-interval!)

; @usage
;  [:x.environment/set-timeout! :my-timeout {:event [:my-event] :timeout 420}]
(r/reg-fx :x.environment/set-timeout! set-timeout!)

; @usage
;  [:x.environment/clear-timeout! :my-timeout]
(r/reg-fx :x.environment/clear-timeout! clear-timeout!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/update-window-data! update-window-data!)
