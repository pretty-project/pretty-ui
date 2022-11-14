
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.environment.cookie-handler.side-effects
    (:require [goog.net.cookies                     :as goog.net.cookies]
              [re-frame.api                         :as r]
              [x.environment.cookie-handler.config  :as cookie-handler.config]
              [x.environment.cookie-handler.helpers :as cookie-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn store-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  ;  {:cookie-type (keyword)
  ;   :max-age (sec)
  ;   :secure (boolean)
  ;   :same-site (string)
  ;   :value (*)}
  [cookie-id {:keys [max-age secure same-site value] :as cookie-props}]
  (let [cookie-name (cookie-handler.helpers/cookie-id->cookie-name cookie-id cookie-props)
        cookie-body (str {:cookie-id cookie-id :value value})]
       (try (.set goog.net.cookies cookie-name cookie-body #js{:domain   cookie-handler.config/COOKIE-DOMAIN
                                                               :path     cookie-handler.config/COOKIE-PATH
                                                               :maxAge   max-age
                                                               :sameSite same-site
                                                               :secure   secure})
            (r/dispatch [:x.environment/cookie-set cookie-id cookie-props]))))

(defn remove-browser-cookie!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) cookie-id
  ; @param (map) cookie-props
  [cookie-id cookie-props]
  (let [cookie-name (cookie-handler.helpers/cookie-id->cookie-name cookie-id cookie-props)]
       (try (.remove goog.net.cookies cookie-name cookie-handler.config/COOKIE-PATH cookie-handler.config/COOKIE-DOMAIN)
            (r/dispatch [:x.environment/cookie-removed cookie-id]))))

(defn remove-browser-cookies!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (try (.clear goog.net.cookies)
       (r/dispatch [:x.environment/cookies-removed])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/store-browser-cookie! store-browser-cookie!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/remove-browser-cookie! remove-browser-cookie!)

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.environment/remove-browser-cookies! remove-browser-cookies!)
